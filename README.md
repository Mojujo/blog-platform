# Blog Platform Microservice
A basic **REST:ful** **Blog-platform Microservice** built with **Spring Boot, PostgreSQL, React Native, RabbitMQ and Docker**. <br />
The project demonstrates clean microservice architecture using **DTOs, DAOs, Mappers, input validation and JWT-based authentication with Spring Security**. <br />
The microservice communicates through an API-Gateway to ensure optimal security and allows users to send requests between services using REST:ful methods and controllers. <br />

## Table of Contents

- [Features](#features)
- [Project Structure](#project-structure)
- [Technoloiges](#technologies)

## Features

- User authentication with JWT (register & login)
- Safe JWT & Cookie handling to protect the users
- CRUD operations
- DTO & Mapper layers for clean separation of persistence and API models
- Input validation with error messaging
- Global exception handling
- Secure endpoints (authenticated access & roll based access)
- PostgreSQL persistence (local or Supabase)
- API Gateway for seamless requests
- RabbitMQ event handling and event-log database persistence
- Native React SPA served through Nginx for ease of testing
- Docker support

## Project Structure

```
**Service Structure**

blog-platform/services/
    audit-service/
        │── config         # Configurations
        │── repository     # DAO layer (Spring Data PostgreSQL)
        │── listener       # RabbitMQ Listener
        │── model          # PostgreSQL entities
        │── util           # Utility methods
        
   gateway-service/
        │── config         # Cors configuration
        └── app...yml      # Routing and hosting
        
    blog-service/
    user-service/
        │── config         # Configurations
        │── controller     # REST controllers
        │── exceptions     # Global Exception Handler
        │── repository     # DAO layer (Spring Data PostgreSQL)
        │─┐── model        # PostgreSQL entities
        │ │── dto          # Data Transfer Objects
        │ └── mapper       # DTO ↔ Entity mappers
        │── security       # JWT Utility & Filter
        │── service        # Business logic
        │── util           # Utility methods
        
blog-platform/react/react-frontend
```

## Technologies

- Java 21
- Spring Boot 3
- Spring Security
- Spring Cloud Gateway
- PostgreSQL
- JWT
- RabbitMQ
- React Native
- Nginx
- Maven
- Docker / Docker Compose