#!/bin/sh 

npm start &
java -jar parkship.jar &
nginx -g "daemon off;"