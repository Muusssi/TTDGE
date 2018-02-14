#!/bin/bash


# Create a temporary library for the class files
mkdir -p tmp
# Compile the jave code
javac src/ttdge/*.java -d tmp/ -cp core.jar
cd tmp
# Collect to jar
jar cvf ../library/TTDGE/library/ttdge.jar ttdge/*.class
cd ..
# Remove tmp folder
rm -r tmp
# Copy source code to the library
mkdir -p library/TTDGE/src
cp src/ttdge/*.java library/TTDGE/src/
# Archive the library
zip -r library/TTDGE.zip library/TTDGE

