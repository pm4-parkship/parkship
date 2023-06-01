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
    public ApplicationUser allowUser(@AuthenticationPrincipal UserEntityInfo user) {
        return user;
    }

    @GetMapping("/admin")
    @Secured("ADMIN")
    public ApplicationUser allowAdmin(@AuthenticationPrincipal UserEntityInfo user) {
        return user;
    }
}

```

The following tables show you the relationship and data that exists on startup.

**Benutzer:**

| Benutzer | Rolle  | Status   |
| -------- | ------ | -------- |
| Lukas    | Benutzer | Entsperrt |
| Nina     | Benutzer | Entsperrt |
| Simon    | Benutzer | Entsperrt |
| Laura    | Benutzer | Entsperrt |
| Harry    | Admin  | Entsperrt |

**Parkplätze:**

| Parkplatz   | Besitzer | Status  | Tags                                  | Preis |
| ----------- | -------- | ------- | ------------------------------------- | ----- |
| Parkplatz 1 | Laura    | Aktiv   | Überwacht, Zugangskontrolle           | 150   |
| Parkplatz 2 | Laura    | Aktiv   | Ladestation, Überwacht, Zugangskontrolle | 180   |
| Parkplatz 3 | Laura    | Inaktiv | Überdacht, Nahverkehrsanbindung       | 160   |
| Parkplatz 4 | Lukas    | Aktiv   | Überdacht, Schatten, Niedrige Einfahrtshöhe | 170   |

**Angebote:**

| Angebot    | Parkplatz   | Verfügbarkeit                 | Zeitraum              |
| ----------- | ----------- | ----------------------------- | --------------------- |
| Angebot 1 | Parkplatz 1 | Mo, Di | 03.07.2023 - 04.07.2023 |
| Angebot 2 | Parkplatz 4 | Mo, Di, Mi, Do, Fr, Sa, So | 03.07.2023 - 29.10.2023 |
| Angebot 3 | Parkplatz 2 | Mo, Di, Mi, Do, Fr | 18.07.2023 - 25.08.2023 |

**Reservierungen:**

| Reservierung   | Parkplatz   | Mieter | Zeitraum              |
| -------------- | ----------- | ------ | --------------------- |
| Reservierung 1 | Parkplatz 4 | Simon  | 03.07.2023 - 04.07.2023 |
| Reservierung 2 | Parkplatz 4 | Nina   | 05.08.2023 - 06.08.2023 |
| Reservierung 3 | Parkplatz 4 | Laura  | 17.09.2023 - 18.09.2023 |
| Reservierung 4 | Parkplatz 4 | Admin  | 28.10.2023 - 29.10.2023 |
| Reservierung 5 | Parkplatz 2 | Nina   | 18.07.2023 - 20.07.2023 |
| Reservierung 6 | Parkplatz 2 | Simon  | 23.08.2023 - 25.08.2023 |

