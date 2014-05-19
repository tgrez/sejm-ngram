#!/bin/bash

cd ..
mvn clean install

cd rest-server
java -jar target/rest-server.jar server config.yml
