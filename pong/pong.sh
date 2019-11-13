#!/bin/bash
#PROCESSING=~/Downloads/processing-3.3.5/core/library/core.jar
PROCESSING=/opt/processing/core/library/core.jar
javac -cp $PROCESSING Pong.java
java -cp $PROCESSING:. Pong
