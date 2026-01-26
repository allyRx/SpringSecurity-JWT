# Spring Security JWT Authentication

A Spring Boot application demonstrating JWT-based authentication with Spring Security, MariaDB, and Docker support.

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation with Docker](#installation-with-docker)
- [Environment Configuration](#environment-configuration)
- [API Endpoints](#api-endpoints)
- [Project Structure](#project-structure)

## Features

- User registration and login with JWT authentication
- Secure password storage with Spring Security
- Role-based access control
- Stateless session management
- CORS support
- Swagger/OpenAPI documentation
- Docker Compose setup with MariaDB, Adminer, and SMTP4Dev

## Prerequisites

- Docker and Docker Compose
- Maven (for local development without Docker)
- Java 17 or higher
- Git

## Installation with Docker

### Quick Start

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd SpringSecurity-JWT
   ```

2. **Start Docker containers**

   ```bash
   docker-compose -f compose.yml up -d
   ```

   This will start:
   - **MariaDB** (port 3306 → 3309 on host)
   - **Adminer** (port 8080 → 8082 on host, for database management)
   - **SMTP4Dev** (port 80 → 5000 and 25 → 1025 on host, for email testing)

3. **Build and run the application**

   ```bash
   # Using Maven wrapper
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```

   Or with Docker (if you have a Dockerfile):

   ```bash
   docker build -t spring-security-jwt:latest .
   docker run -p 8080:8080 --network springequality-jwt_default spring-security-jwt:latest
   ```

4. **Access the application**

   - Application API: `http://localhost:8080/api`
   - Swagger Documentation: `http://localhost:8080/api/swagger-ui.html`
  

## Environment Configuration

Create a `.env` file in the project root directory with the following variables:

```env
# Database Configuration
DB_URL=jdbc:mariadb://localhost:3309/authentication_db
DB_USERNAME=root
DB_PASSWORD=root

# JWT Configuration
JWT_SECRET=your_secret_key_here_minimum_256_bits_or_more_for_security
JWT_EXPIRATION=86400000
```


### Docker Environment Variables

When running in Docker, modify the `DB_URL` to point to the MariaDB container:

```env
DB_URL=jdbc:mariadb://db:3306/authentication_db
DB_USERNAME=root
DB_PASSWORD=root
JWT_SECRET=your_secret_key_here_minimum_256_bits_or_more_for_security
JWT_EXPIRATION=86400000
```

## API Endpoints

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### 1. **Register User**
- **Method**: `POST`
- **Endpoint**: `/auth/signup`
- **Authentication**: Not required
- **Description**: Register a new user account

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Response (200 OK)**:
```json
{
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "createdAt": "2026-01-26T10:30:00Z"
}
```

---

#### 2. **Login User**
- **Method**: `POST`
- **Endpoint**: `/auth/login`
- **Authentication**: Not required
- **Description**: Authenticate user and receive JWT token

**Request Body**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Response (200 OK)**:
```json
{
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken":"dfdkflkdlfkdl.....",
  "expiresIn": 86400000
}

}
```

---

#### 3. **Get All Users**
- **Method**: `GET`
- **Endpoint**: `/auth/user`
- **Authentication**: Required (Bearer Token)
- **Description**: Retrieve all registered users

**Request Headers**:
```
Authorization: Bearer <JWT_TOKEN>
```

**Response (200 OK)**:
```json
[
  {
    "id": 1,
    "email": "user@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "createdAt": "2026-01-26T10:30:00Z"
  }
]
```

#### 4. Refresh token
- **Method**: `POST`
- **Endpoint**: `/auth/refresh`

**Request Body**:
```json
{
  "refreshToken": "refresh token"
}
```

**Response (200 OK)**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken":"dfdkflkdlfkdl.....",
  "expiresIn": 86400000
}
```


#### 5. Logout
- **Method**: `POST`
- **Endpoint**: `/auth/logout`
```


---

## Usage Examples

### Using cURL

**1. Register a user**:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securepass123",
    "firstName": "John",
    "lastName": "Doe"
  }'
```

**2. Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "securepass123"
  }'
```

**3. Get all users** (replace TOKEN with the JWT token received from login):
```bash
curl -X GET http://localhost:8080/api/auth/user \
  -H "Authorization: Bearer TOKEN"
```

### Using Postman

1. Create a new POST request to `http://localhost:8080/api/auth/signup`
2. Set Body as JSON and add the user registration data
3. Send the request to register
4. Use the same steps for login to get the JWT token
5. For authenticated endpoints, add the token to the Authorization header as `Bearer <TOKEN>`



## Security Configuration

- **CSRF Protection**: Disabled (suitable for stateless JWT authentication)
- **Session Management**: Stateless (no server-side session storage)
- **CORS**: Enabled for `http://localhost:8005`
- **Public Endpoints**: `/auth/signup`, `/auth/login`, `/swagger-ui/**`, `/v3/api-docs/**`
- **Protected Endpoints**: All other endpoints require valid JWT token

## Database Management

Access the database through Adminer at `http://localhost:8082`:

- **Server**: `db` (when using Docker network)
- **Username**: `root`
- **Password**: `root`

## Troubleshooting

### Database Connection Error
- Ensure MariaDB container is running: `docker ps`
- Verify database URL in `.env` file
- Check database credentials

### JWT Token Invalid
- Ensure `JWT_SECRET` is set correctly in `.env`
- Verify token hasn't expired (check `JWT_EXPIRATION`)
- Include `Bearer` prefix in Authorization header

### CORS Error
- Check allowed origins in `SecurityConfig.java`
- Verify request origin matches configuration


## Support

For issues or questions, please open an issue in the repository.
