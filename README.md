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
    blog-service/
    user-service/
    audit-service/
        │── config         # Configurations
        │── controller     # REST controllers
        │── exceptions     # Global Exception Handler
        │── Repository     # DAO layer (Spring Data PostgreSQL)
        │─┐── Model        # PostgreSQL entities
        │ │── dto          # Data Transfer Objects
        │ └── mapper       # DTO ↔ Entity mappers
        │── Security       # JWT Utility & Filter
        │── Service        # Business logic
        
blog-platform/react
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