# Parkship

## How to develop locally:

### Prerequisites

- Intellij
- node.js 18 installed


### Setup

Clone the repository

```bash
$ git clone https://github.com/pm4-parkship/parkship.git
```

Open the folder backend in intellij and run the spring boot application. Backend endpoints will be available under http://localhost:8080/api

Open a terminal in the frontend folder and install the npm packages:

```bash
  $ npm install
```

Run the application with

```bash
  $ npm run dev
```

The application will be available under http://localhost:3000

Hint: 

fetch requests can be sent to http://localhost/api/...

The requests will be proxied to the backend on Port 8080.

<br/>

## Local build (only need if you want to change something in the docker container)

Rename the file .env.example to env, then do:

```bash
./build.sh
```
(Needs node.js and java in path)

# Build container

```bash
docker compose build && docker compose up
```

# Azure

https://parkship-app.azurewebsites.net/
(Sometimes it's sleeping and you get an 502..be patient)

# Dockerhub

https://hub.docker.com/repository/docker/edelmetall/parkship/general
