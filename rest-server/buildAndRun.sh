#!/bin/bash

cd ..
mvn clean install -Pserver

cd rest-server
java -jar target/rest-server.jar server hello-world.yml
