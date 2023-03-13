FROM node:19-alpine3.16

WORKDIR /usr/app
COPY ./ /usr/app

RUN npm install && \
    find /usr/app/node_modules/ ! -user root | xargs chown root:root

EXPOSE 3000
ENTRYPOINT ["npm", "run", "dev"]

