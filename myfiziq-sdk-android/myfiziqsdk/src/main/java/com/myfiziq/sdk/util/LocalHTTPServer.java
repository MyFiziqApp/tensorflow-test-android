package com.myfiziq.sdk.util;

import android.os.Looper;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @hide
 */
public class LocalHTTPServer implements Runnable
{
    private int SERVER_PORT = 0;
    private Thread thread;
    private boolean isRunning;
    private ServerSocket socket;
    private int port;
    String mContentType;
    byte[] mData;

    public LocalHTTPServer(String contentType, byte[] data)
    {
        mContentType = contentType;
        mData = data;

        // Create listening socket
        try
        {
            socket = new ServerSocket(SERVER_PORT, 0, InetAddress.getByAddress(new byte[]{127, 0, 0, 1}));
            socket.setSoTimeout(5000);
            port = socket.getLocalPort();
        }
        catch (UnknownHostException e)
        { // impossible
        }
        catch (IOException e)
        {
            //Timber.e(e, "IOException initializing server");
        }

    }

    public String getUrl() throws Exception
    {
        String url = String.format("http://127.0.0.1:%d", port);

        return url;
    }

    public void start()
    {
        thread = new Thread(this);
        thread.start();
    }

    public void stop()
    {
        isRunning = false;
        thread.interrupt();
        try
        {
            thread.join(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        Looper.prepare();
        isRunning = true;
        while (isRunning)
        {
            try
            {
                Socket client = socket.accept();
                if (client == null)
                {
                    continue;
                }
                //Timber.d("client connected");

                StreamToMediaPlayerTask task = new StreamToMediaPlayerTask(client, mContentType, mData);
                if (task.processRequest())
                {
                    task.execute();
                }

            }
            catch (SocketTimeoutException e)
            {
                // Do nothing
            }
            catch (IOException e)
            {
                //Timber.e(e, "Error connecting to client");
            }
        }
        //Timber.d("Proxy interrupted. Shutting down.");
    }

    private class StreamToMediaPlayerTask
    {

        Socket client;
        long cbSkip;
        private Properties parameters;
        private Properties request;
        private Properties requestHeaders;
        private String filePath;
        String mContentType;
        byte[] mData;

        public StreamToMediaPlayerTask(Socket client, String contentType, byte[] data)
        {
            this.client = client;
            mContentType = contentType;
            mData = data;
        }

        public boolean processRequest() throws IOException
        {
            // Read HTTP headers
            InputStream is = client.getInputStream();
            final int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            int splitByte = 0;
            int readLength = 0;
            {
                int read = is.read(buffer, 0, bufferSize);
                while (read > 0)
                {
                    readLength += read;
                    splitByte = findHeaderEnd(buffer, readLength);
                    if (splitByte > 0)
                        break;
                    read = is.read(buffer, readLength, bufferSize - readLength);
                }
            }

            // Create a BufferedReader for parsing the header.
            ByteArrayInputStream hbis = new ByteArrayInputStream(buffer, 0, readLength);
            BufferedReader hin = new BufferedReader(new InputStreamReader(hbis));
            request = new Properties();
            parameters = new Properties();
            requestHeaders = new Properties();

            try
            {
                decodeHeader(hin, request, parameters, requestHeaders);
            }
            catch (InterruptedException e1)
            {
                //Timber.e("Exception: " + e1.getMessage());
                e1.printStackTrace();
            }
            for (Map.Entry<Object, Object> e : requestHeaders.entrySet())
            {
                //Timber.i("Header: " + e.getKey() + " : " + e.getValue());
            }

            String range = requestHeaders.getProperty("range");
            if (range != null)
            {
                //Timber.i("range is: " + range);
                range = range.substring(6);
                int charPos = range.indexOf('-');
                if (charPos > 0)
                {
                    range = range.substring(0, charPos);
                }
                cbSkip = Long.parseLong(range);
                //Timber.i("range found!! " + cbSkip);
            }

            if (!request.get("method").equals("GET"))
            {
                //Timber.e("Only GET is supported");
                return false;
            }

            filePath = request.getProperty("uri");

            return true;
        }

        protected void execute()
        {
            ExternalResourceDataSource dataSource = new ExternalResourceDataSource(mContentType, mData);
            long fileSize = dataSource.getContentLength();

            String headers = "";
            if (cbSkip > 0)
            {// It is a seek or skip request if there's a Range
                // header
                headers += "HTTP/1.1 206 Partial Content\r\n";
                headers += "Content-Type: " + dataSource.getContentType() + "\r\n";
                headers += "Accept-Ranges: bytes\r\n";
                headers += "Content-Length: " + (fileSize - cbSkip) + "\r\n";
                headers += "Content-Range: bytes " + cbSkip + "-" + (fileSize - 1) + "/" + fileSize + "\r\n";
                headers += "Connection: Keep-Alive\r\n";
                headers += "\r\n";
            }
            else
            {
                headers += "HTTP/1.1 200 OK\r\n";
                headers += "Content-Type: " + dataSource.getContentType() + "\r\n";
                headers += "Accept-Ranges: bytes\r\n";
                headers += "Content-Length: " + fileSize + "\r\n";
                headers += "Connection: Keep-Alive\r\n";
                headers += "\r\n";
            }

            //Timber.i("headers: " + headers);

            OutputStream output = null;
            byte[] buff = new byte[64 * 1024];
            try
            {
                output = new BufferedOutputStream(client.getOutputStream(), 32 * 1024);
                output.write(headers.getBytes());
                InputStream data = dataSource.getInputStream();

                dataSource.skipFully(data, cbSkip);//try to skip as much as possible

                // Loop as long as there's stuff to send and client has not closed
                int cbRead;
                while (!client.isClosed() && (cbRead = data.read(buff, 0, buff.length)) != -1)
                {
                    output.write(buff, 0, cbRead);
                }
            }
            catch (SocketException socketException)
            {
                //Timber.e("SocketException() thrown, proxy client has probably closed. This can exit harmlessly");
            }
            catch (Exception e)
            {
                //Timber.e("Exception thrown from streaming task:");
                //Timber.e(e.getClass().getName() + " : " + e.getLocalizedMessage());
            }

            // Cleanup
            try
            {
                if (output != null)
                {
                    output.close();
                }
                client.close();
            }
            catch (IOException e)
            {
                //Timber.e("IOException while cleaning up streaming task:");
                //Timber.e(e.getClass().getName() + " : " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }

        /**
         * Find byte index separating header from body. It must be the last byte of
         * the first two sequential new lines.
         **/
        private int findHeaderEnd(final byte[] buf, int rlen)
        {
            int splitbyte = 0;
            while (splitbyte + 3 < rlen)
            {
                if (buf[splitbyte] == '\r' && buf[splitbyte + 1] == '\n'
                        && buf[splitbyte + 2] == '\r' && buf[splitbyte + 3] == '\n')
                    return splitbyte + 4;
                splitbyte++;
            }
            return 0;
        }


        /**
         * Decodes the sent headers and loads the data into java Properties' key -
         * value pairs
         **/
        private void decodeHeader(BufferedReader in, Properties pre,
                                  Properties parms, Properties header) throws InterruptedException
        {
            try
            {
                // Read the request line
                String inLine = in.readLine();
                if (inLine == null)
                    return;
                StringTokenizer st = new StringTokenizer(inLine);
                //if (!st.hasMoreTokens())
                    //Timber.e("BAD REQUEST: Syntax error. Usage: GET /example/file.html");

                String method = st.nextToken();
                pre.put("method", method);

                //if (!st.hasMoreTokens())
                    //Timber.e("BAD REQUEST: Missing URI. Usage: GET /example/file.html");

                String uri = st.nextToken();

                // Decode parameters from the URI
                int qmi = uri.indexOf('?');
                if (qmi >= 0)
                {
                    decodeParms(uri.substring(qmi + 1), parms);
                    uri = decodePercent(uri.substring(0, qmi));
                }
                else
                    uri = decodePercent(uri);

                // If there's another token, it's protocol version,
                // followed by HTTP headers. Ignore version but parse headers.
                // NOTE: this now forces header names lowercase since they are
                // case insensitive and vary by client.
                if (st.hasMoreTokens())
                {
                    String line = in.readLine();
                    while (line != null && line.trim().length() > 0)
                    {
                        int p = line.indexOf(':');
                        if (p >= 0)
                            header.put(line.substring(0, p).trim().toLowerCase(),
                                    line.substring(p + 1).trim());
                        line = in.readLine();
                    }
                }

                pre.put("uri", uri);
            }
            catch (IOException ioe)
            {
                //Timber.e("SERVER INTERNAL ERROR: IOException: " + ioe.getMessage());
            }
        }

        /**
         * Decodes parameters in percent-encoded URI-format ( e.g.
         * "name=Jack%20Daniels&pass=Single%20Malt" ) and adds them to given
         * Properties. NOTE: this doesn't support multiple identical keys due to the
         * simplicity of Properties -- if you need multiples, you might want to
         * replace the Properties with a Hashtable of Vectors or such.
         */
        private void decodeParms(String parms, Properties p)
                throws InterruptedException
        {
            if (parms == null)
                return;

            StringTokenizer st = new StringTokenizer(parms, "&");
            while (st.hasMoreTokens())
            {
                String e = st.nextToken();
                int sep = e.indexOf('=');
                if (sep >= 0)
                    p.put(decodePercent(e.substring(0, sep)).trim(),
                            decodePercent(e.substring(sep + 1)));
            }
        }

        /**
         * Decodes the percent encoding scheme. <br/>
         * For example: "an+example%20string" -> "an example string"
         */
        private String decodePercent(String str) throws InterruptedException
        {
            try
            {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < str.length(); i++)
                {
                    char c = str.charAt(i);
                    switch (c)
                    {
                        case '+':
                            sb.append(' ');
                            break;
                        case '%':
                            sb.append((char) Integer.parseInt(
                                    str.substring(i + 1, i + 3), 16));
                            i += 2;
                            break;
                        default:
                            sb.append(c);
                            break;
                    }
                }
                return sb.toString();
            }
            catch (Exception e)
            {
                //Timber.e("BAD REQUEST: Bad percent-encoding.");
                return null;
            }
        }
    }

    /**
     * provides meta-data and access to a stream for resources on SD card.
     */
    protected class ExternalResourceDataSource
    {

        String mContentType;
        byte[] mData;

        public ExternalResourceDataSource(String contentType, byte[] data)
        {
            mContentType = contentType;
            mData = data;
        }

        /**
         * Discards {@code n} bytes of data from the input stream. This method
         * will block until the full amount has been skipped. Does not close the
         * stream.
         *
         * @param in the input stream to read from
         * @param n  the number of bytes to skip
         * @throws EOFException if this stream reaches the end before skipping all
         *                      the bytes
         * @throws IOException  if an I/O error occurs, or the stream does not
         *                      support skipping
         */
        public void skipFully(InputStream in, long n) throws IOException
        {
            while (n > 0)
            {
                long amt = in.skip(n);
                if (amt == 0)
                {
                    // Force a blocking read to avoid infinite loop
                    if (in.read() == -1)
                    {
                        throw new EOFException();
                    }
                    n--;
                }
                else
                {
                    n -= amt;
                }
            }
        }


        /**
         * Returns a MIME-compatible content type (e.g. "text/html") for the
         * resource. This method must be implemented.
         *
         * @return A MIME content type.
         */
        public String getContentType()
        {
            return mContentType;
        }

        /**
         * Returns the length of resource in bytes.
         * <p>
         * By default this returns -1, which causes no content-type header to be
         * sent to the client. This would make sense for a stream content of
         * unknown or undefined length. If your resource has a defined length
         * you should override this method and return that.
         *
         * @return The length of the resource in bytes.
         */
        public long getContentLength()
        {
            return mData.length;
        }

        public InputStream getInputStream()
        {
            return new ByteArrayInputStream(mData);
        }
    }
}