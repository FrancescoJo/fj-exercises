#!/bin/bash
SDK_PATH=MediaMonkeyPluginSdk
BUILD_FILE=build.gradle
BUILD_FILE_PATH=$SDK_PATH/$BUILD_FILE
AAR_FILE=MediaMonkeyPluginSdk-release.aar
AAR_FILE_PATH=$SDK_PATH/build/outputs/aar/$AAR_FILE
LOCAL_BUILD_DIR=build

if [ ! -f $BUILD_FILE_PATH ]; then
	echo "Build file $BUILD_FILE is not found on `pwd`$SDK_PATH."
	exit 1
fi

`pwd`/gradlew -b $BUILD_FILE_PATH assembleRelease
cp $AAR_FILE_PATH $LOCAL_BUILD_DIR/
if [ -f $LOCAL_BUILD_DIR/$AAR_FILE ]; then
	echo "Library file $AAR_FILE is successfully copied into $LOCAL_BUILD_DIR/$AAR_FILE";
	exit 0;
else
	echo "Library file $AAR_FILE is not copied into $LOCAL_BUILD_DIR/$AAR_FILE";
	exit 1;
fi
