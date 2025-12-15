
# Media Ratings Platform (MRP)

A standalone **Java 21** application implementing a **RESTful HTTP server** that acts as a backend API for managing media content (movies, series, and games), user ratings, favorites, and recommendations.  
The frontend (Web, Mobile, or Console) is **not part of this project**.

---

## 📑 Table of Contents

- [Project Overview](#-project-overview)
- [Features](#-features)
- [Project Status](#-project-status)
- [Technologies Used](#-technologies-used)
- [Setup and Run](#-setup-and-run)
- [Authorization](#-authorization)
- [Testing](#-testing)
- [API Endpoints](#-api-endpoints)

---

## 🧩 Project Overview

The **Media Ratings Platform (MRP)** provides a REST-based backend that allows users to:

- Register and log in securely
- Manage a personal profile with statistics
- Create, update, delete, and rate media entries
- Mark media as favorites and view rating history
- Receive personalized recommendations

The application is implemented **without frameworks such as Spring or ASP.NET**, using Java’s built-in HTTP server.  
Data is persisted in **PostgreSQL**, running inside a **Docker container**.

---

## 🚀 Features

- **User Management:** Registration, login, profile updates
- **Media Management:** Create, update, delete media entries (creator-only)
- **Ratings:** Rate media (1–5 stars), edit/delete own ratings, like ratings
- **Favorites:** Mark and unmark media as favorites
- **Recommendations:** Based on rating history and genre similarity
- **Search & Filter:** Filter media by genre, type, year, age restriction, rating
- **Statistics & Leaderboard:** User statistics and most active users

---

## 🧱 Project Status

✅ All required features implemented  
✅ REST API fully operational  
✅ PostgreSQL database integrated via Docker  
✅ Token-based authorization implemented  
✅ Unit tests completed (27 tests, all passing)  
✅ Postman collection included for integration testing  

---

## ⚙️ Technologies Used

| Category | Tools & Libraries |
|--------|------------------|
| **Language** | Java 21 |
| **Build Tool** | Maven |
| **Database** | PostgreSQL (Docker Compose) |
| **HTTP Server** | Java HttpServer |
| **Testing** | JUnit 5, Mockito |
| **Security** | BCrypt |
| **JSON Handling** | org.json |
| **Version Control** | Git & GitHub |
| **Architecture** | RESTful API (no frameworks) |

---

## 🐳 Setup and Run

### 1. Clone the repository
```bash
   git clone https://github.com/Hashkeil/MRP.git
   cd MRP
````

### 2. Start PostgreSQL (Docker)

```bash
  docker-compose up -d
```

### 3. Build and run the application

```bash
  mvn clean package
  java -jar target/MRP-1.0-SNAPSHOT.jar
```

---

## 🔐 Authorization

The application uses **token-based authorization**.

* Tokens are generated on successful login
* Tokens must be sent via:

  ```
  Authorization: Bearer <token>
  ```
* All protected endpoints validate the token and user permissions
* Tokens are stored in the database and invalidated on logout

---

## 🧪 Testing

* **27 unit tests** implemented using **JUnit 5** and **Mockito**
* Tests focus on service-layer business logic
* Repositories are mocked to avoid database dependencies
* Authorization rules and edge cases are explicitly tested

Run tests with:

```bash
  mvn test
```

A **Postman collection** is included to demonstrate and validate all API endpoints.

