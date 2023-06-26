FROM nginx:1.23.3

ARG PARKSHIP_ENV
ARG JDBC_DATABASE_URL
ARG JDBC_DATABASE_USERNAME
ARG JDBC_DATABASE_PASSWORD

ENV PARKSHIP_ENV=$PARKSHIP_ENV
ENV NODE_ENV=$PARKSHIP_ENV
ENV SPRING_PROFILES_ACTIVE=$PARKSHIP_ENV
ENV JDBC_DATABASE_URL=$JDBC_DATABASE_URL
ENV JDBC_DATABASE_USERNAME=$JDBC_DATABASE_USERNAME
ENV JDBC_DATABASE_PASSWORD=$JDBC_DATABASE_PASSWORD

RUN apt update -y && apt upgrade -y
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash - && apt install -y nodejs
RUN apt install ca-certificates-java 
RUN apt install openjdk-17-jdk -y


WORKDIR /app

COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY backend/target/parkship.jar .
COPY frontend .
COPY ./run.sh .

RUN chmod +x ./run.sh

EXPOSE 80
CMD /bin/sh -c ./run.sh
