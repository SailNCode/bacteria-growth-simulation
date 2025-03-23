#!/bin/sh
javac -d bin src/*.java  src/bacterium/*.java src/internationalization/*.java src/pres/*.java
cp -r resources bin/
jar cfm Bacteria.jar manifest.txt -C bin .
