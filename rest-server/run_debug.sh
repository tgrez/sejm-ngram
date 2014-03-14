#!/bin/bash

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=5005 -jar target/rest-server.jar server hello-world.yml
