# MovieFlix API

MovieFlix API is a RESTful web service built with **Spring Boot** that allows users to manage movie records with functionalities like adding, updating, deleting, and retrieving movies. It also includes authentication via **OAuth2 (Google & GitHub)** and handles exceptions with **JDBC-based error handling**.

## Features

✅ **Movie Management:** CRUD operations for movies (Add, Update, Delete, Fetch).  
✅ **File Handling:** Upload and serve movie posters securely.  
✅ **Pagination & Sorting:** Fetch movies with pagination and sorting.  
✅ **Authentication:** Google & GitHub OAuth2 authentication using Spring Security.  
✅ **Exception Handling:** Custom exception handling with JDBC integration.  
✅ **Database:** Uses PostgreSQL/MySQL for data persistence.  
✅ **Docker Support:** Ready-to-deploy with Docker.  
✅ **API Documentation:** Integrated Swagger UI.

---

## Tech Stack

- **Spring Boot 3+**  
- **Spring Security (OAuth2, JWT)**  
- **JPA & Hibernate**  
- **MySQL/PostgreSQL**  
- **Maven**  
- **Lombok**  
- **Docker**  
- **Swagger UI**  

---

## Installation & Setup

### 1. Clone the Repository
```sh
$ git clone https://github.com/yourusername/MovieFlix.git
$ cd MovieFlix
```

### 2. Configure Database
Modify `application.properties` or `application.yml` for database configurations.

```properties
server.PORT=8080
spring.application.name=MovieApi

spring.datasource.username=root
spring.datasource.password=bineet123
spring.datasource.url=jdbc:mysql://localhost:3306/movies
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
sprig.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.properties.hibernate.formal_sql=true
project.poster=posters/
base.url=http://localhost:8080
```

### 3. Run the Application

```sh
$ mvn clean install
$ mvn spring-boot:run
```

---

## API Endpoints

### Movie Management
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/api/v1/movie/add-movie` | Add a new movie |
| **GET** | `/api/v1/movie/{movieId}` | Get a movie by ID |
| **GET** | `/api/v1/movie/all` | Fetch all movies |
| **PUT** | `/api/v1/movie/update/{movieId}` | Update movie details |
| **DELETE** | `/api/v1/movie/delete/{movieId}` | Delete a movie |

### File Handling
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/file/upload` | Upload a movie poster |
| **GET** | `/file/{fileName}` | Serve a movie poster |

### Authentication (OAuth2)
| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/oauth2/authorize/google` | Login with Google |
| **GET** | `/oauth2/authorize/github` | Login with GitHub |

---

## OAuth2 Authentication Setup
To enable Google & GitHub login, configure the OAuth credentials in `application.yml`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
            scope: profile, email
          github:
            client-id: YOUR_GITHUB_CLIENT_ID
            client-secret: YOUR_GITHUB_CLIENT_SECRET
            scope: user:email
```

---

## Exception Handling (JDBC-Based)
Custom exceptions are handled using `@ControllerAdvice`. Example:

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
@ExceptionHandler(MovieNotFoundException.class)
public Map<String, String> handleMovieNotFound(MovieNotFoundException ex) {
    return Map.of("error", ex.getMessage());
}
```

---


## Swagger API Docs

Swagger UI is available at:
```
http://localhost:8080/swagger-ui/index.html
```

---

## Contributing
Contributions are welcome! Fork this repository and create a PR.

---

## License
This project is licensed under the MIT License.

