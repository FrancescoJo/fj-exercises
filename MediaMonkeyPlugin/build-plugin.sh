#!/bin/bash
PROJECT_DIR=`pwd`
BUILD_DIR=build
BUILD_TEMP_DIR=tmp
APK_DIR=$BUILD_DIR/outputs/apk
APK_FILE=MediaMonkeyPlugin-release-unsigned.apk
WORK_TEMP_DIR=$BUILD_DIR/$BUILD_TEMP_DIR/plugintmp
WORK_DIR=$BUILD_DIR/$BUILD_TEMP_DIR/plugin
DEX_FILE=classes.dex
CONFIG_DIR=config
PKG_INFO_FILE=pkg-info.xml
PKG_INFO_PATH=$CONFIG_DIR/$PKG_INFO_FILE
OUTPUT_FILE=`basename $PROJECT_DIR`.mmplugin

# Build
./gradlew clean assembleRelease
RESULT=$?
if [ "$RESULT" != "0" ]; then
	echo -e "\nPlease fix compilation error first."
	exit 1;
fi

# Copy intermediates
if [ ! -d "$WORK_TEMP_DIR" ]; then
  mkdir $WORK_TEMP_DIR
fi
unzip $APK_DIR/$APK_FILE -d $WORK_TEMP_DIR/

# Make work dir
if [ ! -d "$WORK_DIR" ]; then
  mkdir $WORK_DIR
fi

# Copy necessary files and make zip
cp $WORK_TEMP_DIR/$DEX_FILE $PKG_INFO_PATH $WORK_DIR
pushd .
cd $WORK_DIR
zip $OUTPUT_FILE *
rm $DEX_FILE $PKG_INFO_FILE
popd
if [ -f "$WORK_DIR/$OUTPUT_FILE" ]; then
	echo -e "\nPlugin file $WORK_DIR/$OUTPUT_FILE is created successfully."
	openssl sha1 $WORK_DIR/$OUTPUT_FILE
	exit 0;
else
	echo -e "\nFailed to create plugin file $WORK_DIR/$OUTPUT_FILE."
	exit 1;
fi