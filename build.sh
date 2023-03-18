#!/usr/bin/bash

cd backend && ./gradlew bootJar
cd ../frontend && npm run build
