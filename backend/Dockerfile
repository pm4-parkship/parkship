FROM eclipse-temurin:17 

# load application from build_stage
ADD /home/runner/work/parkship/parkship/backend/target/parkship-backend.jar parkship-backend.jar

# configure and run container
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/parkship-backend.jar"]
