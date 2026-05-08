# Card Rescue

> **What is lost may yet be found.**

Card Rescue is a Spring Boot backend application for securely reporting, tracking, and recovering lost credit/debit cards.

The system allows users to report lost cards and found cards. When the card type, bank name, and last four digits match, the system creates a match, updates card statuses, and sends email notifications to both users.

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

## Authentication Header

For protected APIs, pass the JWT token in the request header:

```http
Authorization: Bearer <token>
```

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

## Environment Variables

Do not store real email credentials in `application.properties` before pushing to Git.

Use environment variables:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}
```

Required local environment variables:

```text
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

## Database Configuration

Example PostgreSQL configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/card_rescue_db
spring.datasource.username=postgres
spring.datasource.password=your_database_password
```

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

## Run Project

```bash
mvn spring-boot:run
```

Application runs on:

```text
http://localhost:8080
```

## Security Notes

- Passwords are stored using BCrypt hashing.
- JWT is required for protected APIs.
- Admin APIs are restricted to users with role `ADMIN`.
- Email app passwords should never be committed to Git.

## Project Status

Backend is completed with authentication, JWT security, role-based admin access, card matching, dashboard APIs, Flyway migration, global exception handling, and email notification.
