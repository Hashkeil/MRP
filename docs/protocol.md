
# Media Ratings Platform (MRP) – Development Protocol

**Student:** Hashkeil Mahmoud  
**Project:** Media Ratings Platform (MRP)  
**Repository:** https://github.com/Hashkeil/MRP  
**Date:** December 2025  

---

## 1. Project Overview

The Media Ratings Platform (MRP) is a standalone RESTful backend application implemented in **Java 21**.  
It allows users to manage media entries (movies, series, games), rate and review content, mark favorites, and receive personalized recommendations.

The application was implemented **without using frameworks such as Spring or ASP.NET**, as required by the specification.  
PostgreSQL is used for persistence and runs inside Docker.

---

## 2. Architecture & Technical Decisions

The application follows a layered architecture:

```

Controllers → Services → Repositories → Database

````

- **Controllers** handle HTTP requests and responses
- **Services** implement business logic and authorization rules
- **Repositories** manage database access using JDBC
- **Models** represent domain entities

Because Java’s built-in `HttpServer` does not support REST-style paths (e.g. `/api/media/{id}`), a **custom router** was implemented that:
- Splits request paths into segments
- Matches static segments
- Extracts path parameters from `{}` placeholders

---

## 3. Security & Authorization

- **Token-based authorization** using the `Authorization: Bearer <token>` header  
- Tokens are generated on login and stored in the database  
- All protected endpoints validate the token and ownership where required  
- **Passwords are hashed using BCrypt (12 salt rounds)**  
- All database queries use `PreparedStatement`, preventing SQL injection  

---

## 4. Database Design

PostgreSQL is used with the following main tables:
- `users`
- `media_entries`
- `ratings`
- `favorites`
- `genres`

Key design decisions:
- Genres are stored in a separate table to support filtering and recommendations
- Ratings include a `confirmed` flag to support comment moderation
- Unique constraints enforce one rating per user per media
- Foreign keys use `ON DELETE CASCADE` to maintain consistency

---

## 5. Recommendation System

Recommendations are generated based on:
- User rating history
- Genre similarity
- Exclusion of already rated media


---

## 6. SOLID Principles

- **Single Responsibility Principle:**  
  Each service (AuthService, MediaService, RatingService, UserService, etc.) has one clear responsibility.

- **Dependency Inversion Principle:**  
  Services depend on repository interfaces injected via constructors, enabling mocking and isolated unit testing.

---

## 7. Unit Testing Strategy

- **27 unit tests** written using **JUnit 5** and **Mockito**
- Tests focus on **service-layer business logic**
- Repositories are mocked to avoid database dependencies
- Authorization, ownership checks, and edge cases are explicitly tested

Covered services:
- Authentication
- Media management
- Ratings and moderation

- User profiles and statistics


All tests pass successfully using `mvn test`.

---

## 8. Problems Encountered & Solutions

- **Routing without Spring:** solved by implementing a custom router that parses REST-style paths and extracts path parameters.
- **Recommendation logic complexity:** solved by using a content-based approach based on rating history and genre similarity.
- **Database connectivity issues:** solved by running PostgreSQL in Docker and fixing port mapping conflicts.
- **Password handling errors:** solved by introducing BCrypt hashing and validating passwords correctly during login.

---

---

## 9. Time Tracking (Estimated)

| Phase | Hours |
|-----|------|
Project setup & database design | 5 h  
Core features & routing | 15 h  
Advanced features | 17 h  
Testing & integration | 10 h  
Documentation | 5 h  

**Total:** ~52 hours

---

## 10. Technology Stack

- Java 21  
- PostgreSQL   
- Maven  
- Java HttpServer  
- JDBC  
- BCrypt  
- JUnit 5 & Mockito  
- Postman  

---

## 11. Running the Project

```bash
  docker-compose up -d
  mvn clean package
  java -jar target/MRP-1.0-SNAPSHOT.jar
  mvn test
````

