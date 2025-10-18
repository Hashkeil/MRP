# Media Ratings Platform (MRP)

A standalone **Java 21** application implementing a **RESTful HTTP server** that serves as an API for managing media content (movies, series, and games), ratings, and recommendations.  
> The frontend (Web, Mobile, or Console) is **not part of this project**.

---

## 📑 Table of Contents

- [Project Overview](#project-overview)  
- [Features](#features)  
- [Current Progress](#current-progress)  
- [Technologies Used](#technologies-used)  
- [Setup and Run](#setup-and-run)  
- [Testing](#testing)  
- [Future Work](#future-work)  
- [API Endpoints](#api-endpoints)

---

## 🧩 Project Overview

The **Media Ratings Platform (MRP)** provides a backend API that allows users to:

- Register and log in securely  
- Manage personal profiles and statistics  
- Add, update, delete, and rate media entries  
- Mark favorite items and view others’ ratings  
- Receive recommendations based on user rating patterns  

Authentication uses **token-based authorization**, and the database is managed via **PostgreSQL** in a **Docker container**.

---

## 🚀 Features

- **User Management:** Registration, login, profile updates  
- **Media Management:** CRUD operations for media entries  
- **Ratings:** Add, view, and like ratings (1–5 stars)  
- **Favorites:** Mark or unmark favorite media  
- **Recommendations:** Based on user similarity and rating history  
- **Search & Filter:** Find media by genre, rating, or creator  
- **Statistics:** Track total ratings and average scores  

---

## 🧱 Current Progress

✅ Model entities implemented (`User`, `MediaEntry`, `Rating`, `Genre`, `Favorite`)  
✅ Service classes for business logic tested  
✅ PostgreSQL **Docker container** successfully connected  
✅ Entities and database integration tested  
✅ Unit tests (JUnit 5 + Mockito) for models and services  
✅ Basic API endpoints under development  

---

## ⚙️ Technologies Used

| Category | Tools & Libraries |
|-----------|------------------|
| **Language** | Java 21 |
| **Build Tool** | Maven 21 |
| **Database** | PostgreSQL (via Docker Compose) |
| **Testing** | JUnit 5, Mockito |
| **JSON Handling** | Jackson, Gson |
| **Version Control** | Git + GitHub |
| **Architecture** | RESTful API (no Spring Boot) |

---

## 🐳 Setup and Run

### 1. Clone the repository
```bash
git clone https://github.com/Hashkeil/MRP.git
cd MRP
