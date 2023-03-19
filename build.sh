#!/bin/zsh

cd backend &&  ./mvnw clean install
cd ../frontend && npm run build
