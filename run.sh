#!/bin/sh 

npm start &
java -jar backend.jar &
nginx -g "daemon off;"