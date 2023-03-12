FROM eclipse-temurin:17
WORKDIR /backend
EXPOSE 8080
COPY ./target/parkship-backend.jar /backend/parkship-backend.jar
ENTRYPOINT ["java","-jar", "/backend/parkship-backend.jar"]