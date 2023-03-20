# LoremIpsum Crashcourse


## Setup

### Software
- IntelliJ IDEA Ultimate (Als Student gratis) [Herunterladen IntelliJ IDEA](https://www.jetbrains.com/de-de/idea/download/#section=windows)

### Setup IntelliJ IDEA
Download Projekt:
- File > New > Project from Version Control..
- URL -> https://github.com/pm4-parkship/parkship.git

Setup JDK:
- File > Project Structure
- SDK's
![img.png](readme/setupSdk.jpeg)
- Download JDK > 17 und Eclipse Temurin
![img.png](readme/project_setup.jpeg)
- Language lvl 17
## Start Backend
![img.png](readme/startBackend.png)

Hier kann man den Port sehen, auf welchem die Backend Services angeboten werden:
![img.png](readme/tomcatPort.png)

## Zugriff DB
- Link: http://localhost:8080/h2-console/
- JDBC URL: Jenachdem welche Source man in application-dev.properties angegeben hat. (Default: jdbc:h2:mem:test)
- User Name: sa
- Password: 


## Zugriff Swagger UI
- http://localhost:8080/swagger-ui/index.html




# Query methods

```java
public interface UserRepository extends Repository<User, Long> {

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}
```

## Supported keywords inside method names
![img.png](readme/img.png) 
Image Source (https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html)