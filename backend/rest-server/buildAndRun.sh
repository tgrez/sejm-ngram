#!/bin/bash

mvn clean package
java -jar target/rest-server.jar server hello-world.yml
