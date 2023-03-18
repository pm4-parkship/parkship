# Parkship

## Local build

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

https://pm4parkship-test.azurewebsites.net/

(Sometimes it's sleeping and you get an 502..be patient)

# Dockerhub

https://hub.docker.com/repository/docker/rawphl/parkship/general