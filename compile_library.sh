#!/bin/bash


# Create a temporary library for the class files
mkdir -p tmp
mkdir -p tmp/images
cp media/*.png tmp/images/
# Compile the jave code
javac src/ttdge/*.java src/ttdge/tasks/*.java -d tmp/ -cp core.jar
cd tmp
# Collect to jar
jar cf ../library/TTDGE/library/ttdge.jar ttdge/*.class ttdge/tasks/*.class images/*.png
cd ..
# Remove tmp folder
rm -r tmp
# Copy source code to the library
mkdir -p library/TTDGE/src
cp src/ttdge/*.java library/TTDGE/src/
# Archive the library
rm library/TTDGE.zip
zip -r -q --exclude=*.DS_Store* library/TTDGE.zip library/TTDGE

