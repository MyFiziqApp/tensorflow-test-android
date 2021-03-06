@ECHO OFF

REM Please don't delete me. Jenkins needs me to perform static code analysis

cd .\myfiziqsdk-android-cpp

"C:\Program Files\Cppcheck\cppcheck.exe" ^
--enable=all --language=c++ ^
--xml ^
--force ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\abseil-cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\ARM_NEON_2_x86_SSE ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\curlcpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\eigen-git-mirror ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\farmhash ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\flatbuffers ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\FP16 ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\gemmlowp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\mace  ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\tensorflow ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenHaarcascade_frontalface_alt2.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenHaarcascade_frontalface_alt2.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenHaarcascade_profileface.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenHaarcascade_profileface.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlBase.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleAvgVerts.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleAvgVerts.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleBonW.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleBonW.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleBonWInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleBonWInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleCov.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleCov.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleFaces.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleFaces.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleFacesInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleFacesInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleLaplacianRings.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleLaplacianRings.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleLaplacianRingsAsVectors.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleLaplacianRingsAsVectors.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleMvnMu.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleMvnMu.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleRanges.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleRanges.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSkV.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSkV.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSvInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleSvInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleVertsInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecFemaleVertsInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightCalf.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightCalf.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightThigh.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightThigh.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightUpperArm.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecInvRightUpperArm.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleAvgVerts.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleAvgVerts.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleBonW.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleBonW.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleBonWInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleBonWInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleCov.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleCov.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleFaces.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleFaces.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleFacesInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleFacesInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleLaplacianRings.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleLaplacianRings.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleLaplacianRingsAsVectors.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleLaplacianRingsAsVectors.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleMvnMu.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleMvnMu.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleRanges.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleRanges.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSkV.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSkV.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSvInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleSvInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleVertsInv.cpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\MFZAvatarGenMdlSecMaleVertsInv.hpp ^
-i.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp\tests ^
-I.\src\main\cpp\myfiziq-sdk-cpp-utils\include ^
-I.\src\main\cpp\myfiziqJni\include ^
-I.\src\main\cpp\myfiziq-sdk-cpp-utils\myfiziq-sdk-cpp ^
.\src\main\cpp\myfiziqJni ^
.\src\main\cpp\myfiziq-sdk-cpp-utils 2> .\build\cppcheck-output.xml