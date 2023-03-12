FROM openjdk:11 as build_stage

# prepare environment
WORKDIR /opt/app

# copy sources
COPY mvnw ./
COPY .mvn/ .mvn/
COPY pom.xml ./
COPY src/ src/

# build application
RUN ./mvnw --batch-mode install -DskipTests

FROM eclipse-temurin:17 as packaging_stage

# prepare environment
WORKDIR /opt/app

# load application from build_stage
COPY --from=build_stage /opt/app/target/*.jar parkship-backend.jar

# configure and run container
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "parkship-backend.jar"]
