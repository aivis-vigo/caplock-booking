# Caplock Booking

A Spring Boot web application for event booking and management, featuring seat reservation, QR code ticket generation, invoicing, and a waitlist system.

## Tech Stack

- **Java 21** / **Spring Boot 3.5.0**
- **Spring MVC** + **Thymeleaf** (server-side rendering)
- **Spring Data JPA** + **Hibernate** (ORM)
- **H2** (in-memory database, auto-configured)
- **Spring Security** with JWT authentication
- **Google Zxing** for QR code generation
- **Log4j2** for logging
- **Maven** build tool

## Features

- Event creation and management (status tracking, capacity, deadlines)
- Booking management with seat reservation and cancellation
- Ticket generation with QR codes
- Waitlist for fully booked events
- Invoice and payment tracking
- User registration, authentication, and role-based access
- H2 console for development database inspection

## Prerequisites

- Java 21+
- Maven 3.9+ (or use the included Maven wrapper)

## Running the Application

### Using Maven wrapper (recommended)

```bash
./mvnw spring-boot:run
```

### Using system Maven

```bash
mvn spring-boot:run
```

### Build and run JAR

```bash
./mvnw clean package
java -jar target/booking-0.0.1-SNAPSHOT.jar
```

The application starts at **http://localhost:8080**

## Database

The app uses an **H2 in-memory database** — no setup required. The schema is auto-generated from JPA entities on each startup (`ddl-auto=create`).

Access the H2 console at **http://localhost:8080/h2-console**

| Field    | Value                   |
|----------|----------------------   |
| JDBC URL | `jdbc:h2:mem:bookingdb` |
| Username | `sa`                    |
| Password | *(leave blank)*         |

## Configuration

Key settings in `src/main/resources/application.properties`:

| Property | Default | Description |
|---|---|---|
| `server.port` | `8080` | HTTP port |
| `spring.h2.console.enabled` | `true` | Enables H2 web console |
| `spring.jpa.hibernate.ddl-auto` | `update` | Recreates schema on startup |
| `qr.storage.path` | `uploads/qr-codes` | Directory for generated QR images |
| `jwt.secret` | *(dev secret)* | JWT signing key — change in production |

## Project Structure

```
src/main/java/com/caplock/booking/
|-- auth/
├── config/          # Security, web MVC, and mapper config
├── controller/ # Web controllers (events, bookings, users, tickets, QR, etc.)
    |-- view/    
├── entity/
│   ├── dao/         # JPA entities / data access objects
│   └── dto/         # Data transfer objects
├── repository/      # Spring Data repositories
├── service/         # Business logic
    |--impl/    
├── util/            # DTO mappers and utilities
└── exception/       # Custom exceptions

src/main/resources/
├── templates/       # Thymeleaf HTML templates
    |-- fragments/
    |-- ui/
├── static/          # CSS, JS, images
├── application.properties
└── log4j2.xml

uploads/qr-codes/    # Generated QR code images
invoices/
logs/                # Rolling application logs
```

## Logs

Application logs are written to `logs/myapp.log` with daily rotation and a 30-day retention policy. Log level defaults to `INFO`.
