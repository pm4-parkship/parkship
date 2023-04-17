# LoremIpsum Crashcourse

## Setup

### Software

- IntelliJ IDEA Ultimate (Als Student
  gratis) [Herunterladen IntelliJ IDEA](https://www.jetbrains.com/de-de/idea/download/#section=windows)

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

- Link: http://localhost:8080/backend/h2-console/
- JDBC URL: Jenachdem welche Source man in application-dev.properties angegeben hat. (Default: jdbc:h2:mem:test)
- User Name: sa
- Password:

## Zugriff Swagger UI

- http://localhost:8080/backend/swagger-ui/index.html

# Query methods

```java
public interface UserRepository extends Repository<User, Long> {

    List<User> findByEmailAddressAndLastname(String emailAddress, String lastname);
}
```

## Supported keywords inside method names

![img.png](readme/img.png)
Image Source (https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html)

# Security

You can use postman and import the file backend/parkship-backend.postman-collection.json or use http client of your
choice.

## Classes

User class: ch.zhaw.parkship.user.UserEntity

Role class: ch.zhaw.parkship.role.RoleEntity

Currently we have the roleEntities admin and user.

### Endpoints

#### Login

```http request
POST /backend/auth/signup
```

Request:

```json
{
  "email": "test@parkship.ch",
  "password": "test",
  "username": "test"
}
```

Response:

```json
{
  "id": 3,
  "username": "test",
  "roleEntities": [
    "USER"
  ]
}
```

#### Register

```http request
POST /backend/auth/signin
```

Request:

```json
{
  "password": "user",
  "email": "user@parkship.ch"
}
```

Response:

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjgwODkyODYwLCJyb2xlcyI6WyJVU0VSIl0sInVzZXJuYW1lIjoidXNlciJ9.s-Cwm9rl6NCZf7Be04Wk5FfOsBj45_p8LCZ9I9Rbbt0",
  "username": "user",
  "roleEntities": [
    "USER"
  ]
}
```

The jwt token must be sent with every http request in the authorization header:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjgwODkyODYwLCJyb2xlcyI6WyJVU0VSIl0sInVzZXJuYW1lIjoidXNlciJ9.s-Cwm9rl6NCZf7Be04Wk5FfOsBj45_p8LCZ9I9Rbbt0
```

The duration of the token can be set in application.properties. Roles can be used likes this:

```java

@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "Bearer Authentication") // Need for auth to work in swagger
public class AuthTestController {
    @GetMapping("/user")
    //@PreAuthorize("hasRole('ROLE_USER')") // doesn't work?
    //@PreAuthorize("hasRole('USER')") // doesn't work?
    //@PreAuthorize("authentication.principal.id == 1") // works
    //@PreAuthorize("hasAuthority('USER')") // works
    //@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')") // works
    @Secured("USER") // works
    //@Secured({ "USER", "ADMIN" }) // works
    public ApplicationUser allowUser(@AuthenticationPrincipal UserEntity user) {
        return user;
    }

    @GetMapping("/admin")
    @Secured("ADMIN")
    public ApplicationUser allowAdmin(@AuthenticationPrincipal UserEntity user) {
        return user;
    }
}

```