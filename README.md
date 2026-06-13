# Backend Microservices Platform
<img width="1254" height="1254" alt="e2085820-c359-4c1a-bebf-c79e8e8753bd" src="https://github.com/user-attachments/assets/539d5024-287b-4764-afc3-a988aeba29ae" />

## Overview

This repository contains a Java Spring Boot microservices platform built using a distributed architecture. The platform consists of three independently deployable services that communicate through REST APIs and use JWT-based authentication with Role-Based Access Control (RBAC).

The project demonstrates:

* Microservices architecture
* JWT Authentication & Authorization
* Role-Based Access Control (RBAC)
* PostgreSQL persistence
* Redis caching
* Docker containerization
* Docker Compose orchestration
* CI/CD with GitHub Actions
* Automated deployment to AWS EC2

---

## Architecture

### Services

#### Auth Service

Responsible for:

* User registration
* User login
* JWT access token generation
* Refresh token generation
* Token refresh
* Logout

#### Order Service

Responsible for:

* Creating orders
* Listing orders
* Order cancellation
* Role-based authorization

#### Inventory Service

Responsible for:

* Inventory management
* Stock reservation
* Stock release
* Admin stock updates
* Optimistic locking

### Infrastructure Components

| Component      | Purpose                           |
| -------------- | --------------------------------- |
| PostgreSQL     | Persistent relational database    |
| Redis          | Caching and token/session storage |
| Docker         | Containerization                  |
| Docker Compose | Multi-container orchestration     |
| GitHub Actions | CI/CD automation                  |
| AWS EC2        | Production hosting                |

---

## Authentication & Security

The platform uses JWT-based security across all services.

### Features

* JWT Access Tokens
* Refresh Tokens
* Stateless Authentication
* Local JWT Validation
* Role-Based Access Control (RBAC)

### Roles

#### USER

Can:

* Login
* Create orders
* View orders
* Reserve inventory

#### ADMIN

Can perform all USER operations plus:

* Cancel orders
* Update inventory stock

---

## Service Communication Flow

```text
Client
   │
   ▼
Auth Service
   │
   ▼
JWT Token Issued
   │
   ▼
Order Service
   │
   ▼
Inventory Service
```

The Auth Service issues JWT tokens which are validated locally by downstream services without requiring centralized authentication requests.

---

## Technology Stack

### Backend

* Java 17
* Spring Boot 3
* Spring Security
* Spring Data JPA
* Maven

### Database & Cache

* PostgreSQL 16
* Redis 7

### DevOps

* Docker
* Docker Compose
* GitHub Actions
* Docker Hub
* AWS EC2

---

## Prerequisites

Before running the project, ensure the following are installed:

* Java 17
* Maven 3.9+
* Docker
* Docker Compose

---

## Local Development

### Build All Services

```bash
cd backend-microservices/auth-service
mvn clean package

cd ../order-service
mvn clean package

cd ../inventory-service
mvn clean package
```

### Start All Services

From repository root:

```bash
docker compose up --build
```

### Stop Services

```bash
docker compose down
```

---

## Service Ports

| Service           | External Port | Internal Port |
| ----------------- | ------------: | ------------: |
| auth-service      |          8081 |          8080 |
| order-service     |          8082 |          8080 |
| inventory-service |          8083 |          8080 |
| PostgreSQL        |          5432 |          5432 |
| Redis             |          6379 |          6379 |

---

## API Documentation

Swagger UI endpoints:

### Auth Service

```text
http://localhost:8081/swagger-ui/index.html
```

### Order Service

```text
http://localhost:8082/swagger-ui/index.html
```

### Inventory Service

```text
http://localhost:8083/swagger-ui/index.html
```

---

## API Examples

Example API requests are available in:

```text
api-curls.txt
```

Notes:

* Inventory reserve/release endpoints require a valid JWT token.
* Inventory stock update requires ADMIN privileges.
* Order cancellation requires ADMIN privileges.
* JWT token extraction examples use `jq`.

---

## Environment Variables

### Common Variables

| Variable    | Description               |
| ----------- | ------------------------- |
| SERVER_PORT | Service port              |
| DB_URL      | PostgreSQL connection URL |
| DB_USERNAME | Database username         |
| DB_PASSWORD | Database password         |

### Auth Service Variables

| Variable                 | Description                |
| ------------------------ | -------------------------- |
| JWT_ISSUER               | JWT issuer                 |
| JWT_ACCESS_TOKEN_MINUTES | Access token lifetime      |
| JWT_REFRESH_TOKEN_DAYS   | Refresh token lifetime     |
| JWT_PRIVATE_KEY          | Base64 encoded private key |
| JWT_PUBLIC_KEY           | Base64 encoded public key  |

### Order & Inventory Services

| Variable                | Description         |
| ----------------------- | ------------------- |
| JWT_PUBLIC_KEY_LOCATION | Public key location |

---

## CI/CD Pipeline

GitHub Actions automates build, test, packaging, image publishing, and deployment.

### Pull Request Workflow

When a Pull Request is opened against `main`:

1. Checkout source code
2. Setup Java 17
3. Build all services
4. Run tests
5. Validate successful compilation

No deployment occurs during PR validation.

### Main Branch Deployment Workflow

When code is merged into `main`:

1. Build all services
2. Run tests
3. Build Docker images
4. Push images to Docker Hub
5. Connect to AWS EC2 via SSH
6. Pull latest images
7. Restart containers using Docker Compose
8. Deploy updated services

---

## Production Deployment Architecture

```text
Developer
    │
    ▼
GitHub Repository
    │
    ▼
GitHub Actions
    │
    ├── Build
    ├── Test
    ├── Docker Build
    └── Docker Hub Push
    │
    ▼
AWS EC2
    │
    ├── PostgreSQL
    ├── Redis
    ├── Auth Service
    ├── Order Service
    └── Inventory Service
```

---

## Production Deployment

The production environment runs on an AWS EC2 instance using Docker Compose.

Containers running in production:

* auth-service
* order-service
* inventory-service
* postgres
* redis

Deployment is fully automated through GitHub Actions.

No manual server-side builds are required.

---

## Repository Structure

```text
.
├── .github
│   └── workflows
│       └── ci-cd.yml
├── backend-microservices
│   ├── auth-service
│   ├── inventory-service
│   └── order-service
├── docker-compose.yml
├── docker-compose.prod.yml
├── api-curls.txt
└── README.md
```

---

