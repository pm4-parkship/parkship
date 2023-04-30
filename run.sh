#!/bin/sh 

npm start -- -p 3000 &
java -jar -Dspring.profiles.active=production parkship.jar &
nginx -g "daemon off;"
