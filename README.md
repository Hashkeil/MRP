# Media Ratings Platform (MRP)

A standalone Java application implementing a RESTful HTTP server that acts as an API for managing media content (movies, series, and games), ratings, and recommendations. The frontend (Web, Mobile, Console) is **not part of this project**.

---

## Table of Contents

- [Project Overview](#project-overview)  
- [Features](#features)  
- [Current Progress](#current-progress)  
- [Technologies Used](#technologies-used)  
- [Setup and Run](#setup-and-run)  
- [Testing](#testing)  
- [Future Work](#future-work)  
- [API Endpoints](#api-endpoints)  

---

## Project Overview

This project provides a platform where users can:

- Register and log in with unique credentials  
- Manage their profiles and personal statistics  
- Create, update, delete, and rate media entries  
- Like other users' ratings and mark favorites  
- Receive recommendations based on rating behavior  

Authentication will use **token-based authorization**, and the database will be **PostgreSQL** (planned, Docker setup is ready).

---

## Features (Planned)

- User management: registration, login, profile  
- Media management: create, read, update, delete (CRUD)  
- Ratings: star rating (1–5), optional comments, likes  
- Favorites: mark/unmark media entries  
- Recommendations based on user rating behavior and media similarity  
- Searching, filtering, and sorting of media entries  
- Leaderboard of most active users  

---

## Current Progress

✅ Model entities (User, Media, Rating)  
✅ Service classes for business logic  
✅ Controller classes for handling HTTP requests  
✅ Unit tests (JUnit 5) for model entities  

---

## Technologies Used

- **Java 21**  
- **Maven 21** for build and dependency management  
- **JUnit 5** for unit testing  
- PostgreSQL (planned, Docker Compose ready)  
- JSON serialization: Jackson  
- Pure Java HTTP server (no Spring, JSP, or ASP.Net)  

---

## Setup and Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/media-ratings-platform.git
cd media-ratings-platform
