FROM node:latest

WORKDIR /usr/app
COPY ./ /usr/app

RUN npm install

EXPOSE 3000
ENTRYPOINT ["npm", "run", "dev"]

