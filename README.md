# Card Rescue

> **What is lost may yet be found.**

Card Rescue is a Spring Boot backend application for securely reporting, tracking, and recovering lost credit/debit cards.

The system allows users to report lost cards and found cards. When the card type, bank name, and last four digits match, the system creates a match, updates card statuses, and sends email notifications to both users.

---

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Spring Security
- JWT Authentication
- BCrypt Password Hashing
- Spring Data JPA / Hibernate
- PostgreSQL
- Flyway Migration
- Spring Mail / Gmail SMTP
- Docker / Docker Compose

---

## Features

### Authentication

- User registration
- User login
- JWT token generation
- BCrypt password hashing

### User Roles

- USER
- ADMIN

### Lost and Found Card Reporting

- Report lost card
- Report found card
- Match lost and found cards using:
  - Card type
  - Bank name
  - Last four digits

### Match Handling

- Automatic match creation
- Lost/found card status update to `MATCHED`
- Email notification to both lost-card user and found-card user

### Admin Features

- View all users
- Update user details
- Update user role
- Delete users
- Prevent admin from deleting own account
- Restrict admin APIs to ADMIN users only

### Dashboard APIs

- View my lost cards
- View my found cards
- View my matches

### Error Handling

- Global exception handling
- Clean JSON error responses

### Database Management

- PostgreSQL database
- Flyway versioned migrations

### Docker Support

- Spring Boot application container
- PostgreSQL container
- Docker Compose setup

---

## API Endpoints

### Auth

```http
POST /auth/register
POST /auth/login
```

### Lost Cards

```http
POST /lost
GET /lost/my
```

### Found Cards

```http
POST /found
GET /found/my
```

### Matches

```http
GET /matches/my
```

### Admin

```http
GET /admin/users
PUT /admin/users/{id}
DELETE /admin/users/{id}
```

---

## Authentication Header

For protected APIs, pass the JWT token in the request header:

```http
Authorization: Bearer <token>
```

---

## Sample Request Bodies

### Register

```json
{
  "name": "Sahil",
  "email": "sahil@example.com",
  "password": "123456"
}
```

### Login

```json
{
  "email": "sahil@example.com",
  "password": "123456"
}
```

### Lost Card / Found Card

```json
{
  "cardType": "DEBIT",
  "bankName": "HDFC",
  "last4Digits": "1234"
}
```

### Update User by Admin

```json
{
  "name": "Updated User",
  "email": "updated@example.com",
  "role": "USER"
}
```

---

## Environment Variables

Do not store real email credentials or database passwords in Git.

`application.properties` supports environment variables:

```properties
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/card_rescue_db}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:root}

spring.mail.username=${MAIL_USERNAME:}
spring.mail.password=${MAIL_PASSWORD:}
```

Required for email notification:

```text
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

Optional for database:

```text
DB_URL=jdbc:postgresql://localhost:5432/card_rescue_db
DB_USERNAME=postgres
DB_PASSWORD=root
```

---

## Database Configuration

Default local PostgreSQL configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/card_rescue_db
spring.datasource.username=postgres
spring.datasource.password=root
```

---

## Flyway Migration

Migration files are stored in:

```text
src/main/resources/db/migration
```

Example files:

```text
V1__init_schema.sql
V2__add_user_role.sql
```

---

## Run Locally

Make sure PostgreSQL is running locally.

Set environment variables if needed:

```cmd
set DB_PASSWORD=root
set MAIL_USERNAME=your_email@gmail.com
set MAIL_PASSWORD=your_gmail_app_password
```

Run:

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

If port 8080 is already in use, set a different port:

```properties
server.port=${SERVER_PORT:8080}
```

Example:

```cmd
set SERVER_PORT=8081
mvn spring-boot:run
```

---

## Run with Docker

Start Docker Desktop first.

Run:

```bash
docker compose up --build
```

Application runs on:

```text
http://localhost:8080
```

PostgreSQL container:

```text
Database: card_rescue_db
Username: postgres
Password: root
Port: 5432
```

For email notification in Docker, set these environment variables before running Docker:

```text
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

Stop Docker containers:

```bash
docker compose down
```

---

## Docker Services

`docker-compose.yml` includes:

```text
app       -> Spring Boot backend
postgres  -> PostgreSQL 16 database
```

Docker app uses:

```text
DB_URL=jdbc:postgresql://postgres:5432/card_rescue_db
DB_USERNAME=postgres
DB_PASSWORD=root
```

---

## Security Notes

- Passwords are stored using BCrypt hashing.
- JWT is required for protected APIs.
- Admin APIs are restricted to users with role `ADMIN`.
- Gmail app passwords should never be committed to Git.
- Use environment variables for sensitive values.

---

## Project Status

Backend is completed with:

- Authentication
- JWT security
- Role-based admin access
- Lost/found card matching
- Dashboard APIs
- Flyway migration
- Global exception handling
- Email notification
- Docker setup

---

## GitHub Repository

```text
https://github.com/Sahilxviii/Card_Rescue_JAVA.git
```

## Swagger / OpenAPI

Swagger UI is available at:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI JSON is available at:

```text
http://localhost:8080/v3/api-docs
```

For protected APIs, click **Authorize** in Swagger UI and enter your JWT token.

This project uses Swagger/OpenAPI to document and test REST APIs directly from the browser.
