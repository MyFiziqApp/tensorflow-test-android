package com.myfiziq.sdk.views;

// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * A view which renders a series of custom graphics to be overlayed on top of an associated preview
 * (i.e., the camera preview). The creator can add graphics objects, update the objects, and remove
 * them, triggering the appropriate drawing and invalidation within the view.
 *
 * <p>Supports scaling and mirroring of the graphics relative the camera's preview properties. The
 * idea is that detection items are expressed in terms of a preview size, but need to be scaled up
 * to the full view size, and also mirrored in the case of the front-facing camera.
 *
 * <p>Associated {@link Graphic} items should use the following methods to convert to view
 * coordinates for the graphics that are drawn:
 *
 * <ol>
 * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
 * supplied value from the preview scale to the view scale.
 * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
 * coordinate from the preview's coordinate system to the view coordinate system.
 * </ol>
 */
public class GraphicOverlay extends View
{
    private final Object lock = new Object();
    private int previewWidth;
    private float widthScaleFactor = 1.0f;
    private int previewHeight;
    private float heightScaleFactor = 1.0f;
    private boolean cameraFacingFront = true;
    private final List<Graphic> graphics = new ArrayList<>();

    /**
     * Base class for a custom graphics object to be rendered within the graphic overlay. Subclass
     * this and implement the {@link Graphic#draw(Canvas)} method to define the graphics element. Add
     * instances to the overlay using {@link GraphicOverlay#add(Graphic)}.
     */
    public abstract static class Graphic
    {
        private GraphicOverlay overlay;

        public Graphic(GraphicOverlay overlay)
        {
            this.overlay = overlay;
        }

        /**
         * Draw the graphic on the supplied canvas. Drawing should use the following methods to convert
         * to view coordinates for the graphics that are drawn:
         *
         * <ol>
         * <li>{@link Graphic#scaleX(float)} and {@link Graphic#scaleY(float)} adjust the size of the
         * supplied value from the preview scale to the view scale.
         * <li>{@link Graphic#translateX(float)} and {@link Graphic#translateY(float)} adjust the
         * coordinate from the preview's coordinate system to the view coordinate system.
         * </ol>
         *
         * @param canvas drawing canvas
         */
        public abstract void draw(Canvas canvas);

        /**
         * Adjusts a horizontal value of the supplied value from the preview scale to the view scale.
         */
        public float scaleX(float horizontal)
        {
            return horizontal * overlay.widthScaleFactor;
        }

        /**
         * Adjusts a vertical value of the supplied value from the preview scale to the view scale.
         */
        public float scaleY(float vertical)
        {
            return vertical * overlay.heightScaleFactor;
        }

        /**
         * Returns the application context of the app.
         */
        public Context getApplicationContext()
        {
            return overlay.getContext().getApplicationContext();
        }

        /**
         * Adjusts the x coordinate from the preview's coordinate system to the view coordinate system.
         */
        public float translateX(float x)
        {
            if (overlay.cameraFacingFront)
            {
                return overlay.getWidth() - scaleX(x);
            }
            else
            {
                return scaleX(x);
            }
        }

        /**
         * Adjusts the y coordinate from the preview's coordinate system to the view coordinate system.
         */
        public float translateY(float y)
        {
            return scaleY(y);
        }

        public void postInvalidate()
        {
            overlay.postInvalidate();
        }
    }

    public GraphicOverlay(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    /**
     * Removes all graphics from the overlay.
     */
    public void clear()
    {
        synchronized (lock)
        {
            graphics.clear();
        }
        postInvalidate();
    }

    /**
     * Adds a graphic to the overlay.
     */
    public void add(Graphic graphic)
    {
        synchronized (lock)
        {
            graphics.add(graphic);
        }
    }

    /**
     * Removes a graphic from the overlay.
     */
    public void remove(Graphic graphic)
    {
        synchronized (lock)
        {
            graphics.remove(graphic);
        }
        postInvalidate();
    }

    /**
     * Removes all graphics from the overlay that have a specific class.
     */
    public void remove(Class clazz)
    {
        synchronized (lock)
        {
            ListIterator<Graphic> iterator = graphics.listIterator();
            while(iterator.hasNext())
            {
                if(iterator.next().getClass().equals(clazz))
                {
                    iterator.remove();
                }
            }
        }
        postInvalidate();
    }

    /**
     * Sets the camera attributes for size and facing direction, which informs how to transform image
     * coordinates later.
     */
    public void setCameraInfo(int previewWidth, int previewHeight, boolean cameraFacingFront)
    {
        synchronized (lock)
        {
            this.previewWidth = previewWidth;
            this.previewHeight = previewHeight;
            this.cameraFacingFront = cameraFacingFront;
        }
        postInvalidate();
    }

    /**
     * Draws the overlay with its associated graphic objects.
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        synchronized (lock)
        {
            if ((previewWidth != 0) && (previewHeight != 0))
            {
                widthScaleFactor = (float) canvas.getWidth() / (float) previewWidth;
                heightScaleFactor = (float) canvas.getHeight() / (float) previewHeight;
            }

            for (Graphic graphic : graphics)
            {
                graphic.draw(canvas);
            }
        }
    }
}