#!/bin/bash

MAIN_PATH=/home/tomasz/workspace/sejmAPI/sejm-ngram
SEJMOMETR_CLIENT=$MAIN_PATH/sejmometr-client
SEJMOMETR_JAR=$SEJMOMETR_CLIENT/target/sejmometr-client-1.0-SNAPSHOT-jar-with-dependencies.jar

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROPERTIES_FILE_LOCATION=$SCRIPT_DIR/common.properties

java -Dprop.file.loc=$PROPERTIES_FILE_LOCATION -jar $SEJMOMETR_JAR
