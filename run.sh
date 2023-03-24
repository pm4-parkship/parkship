#!/bin/sh 

npm start -- -p 3000 &
java -jar parkship.jar &
nginx -g "daemon off;"
