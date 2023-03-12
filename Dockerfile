FROM eclipse-temurin:17
EXPOSE 8080
ADD ../target/parkship-backend.jar /backend/parkship-backend.jar
ENTRYPOINT ["java","-jar", "/backend/parkship-backend.jar"]