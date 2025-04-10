# Popcorn Palace
A RESTful Movie Ticket Booking System built with Java & Spring Boot.

## Overview  
Popcorn Palace is a backend system that allows managing movies, showtimes, customers, and ticket bookings.  
The system exposes a REST API with full CRUD functionality, seat validation, and constraints on booking behavior.  
It was developed as part of the AT&T TDP 2025 home assignment.

## Tech Stack  
- Java 21  
- Spring Boot 3  
- Hibernate (JPA)  
- MySQL (via Docker Compose)  
- RESTful APIs  
- Testing: JUnit 5, Mockito, MockMvc  
- Build tool: Maven 

### Prerequisites  
- Java 21
- IDE   
- Docker
- Maven

### Run the App
**To get started, clone the repository using: git clone https://github.com/Yfabri7/popcorn-palace.git**
1. Start MySQL using Docker Compose:
   (bash):
   docker-compose up -d 

3. Run the application via your IDE or with Maven:
   (bash):
   mvn spring-boot:run

4. The app will be available at:  
   http://localhost:8080

## Tests
Run all tests with:
(bash):
mvn clean test

Includes unit and integration tests for:
- MovieService / Controller  
- ShowtimeService / Controller  
- TicketBookingService / Controller  
- CustomerService / Controller  
- Total: **79 tests**

## Project Structure
src/
├── main/
│   ├── java/com/att/tdp/popcorn_palace/
│   │   ├── bootstrap/              # Loads sample data using @PostConstruct
│   │   ├── controller/             # REST API endpoints for movies, showtimes, customers, bookings
│   │   ├── dto/                    # DTOs for request/response separation
│   │   ├── exception/              # Custom exception classes and global error handler
│   │   ├── model/                  # JPA entity classes
│   │   ├── repository/             # Spring Data JPA repositories
│   │   ├── service/                # Service interfaces
│   │   └── service/impl/           # Service implementation logic
│   └── resources/
│       ├── application.yaml        # DB configuration and profiles
│       └── schema.sql              # SQL fallback schema (optional)
├── test/
│   └── java/com/att/tdp/popcorn_palace/
│       ├── controller/             # Unit + integration tests for controllers
│       ├── service/                # Unit tests for services
│       └── PopcornPalaceApplicationTests.java # App context load test

## API Endpoints Summary

### Movies
| Action   | Method | Endpoint                  |
|----------|--------|---------------------------|
| Get      | GET    | /movies/{movie_id}        |
| Get All  | GET    | /movies                   |
| Create   | POST   | /movies                   |
| Update   | PUT    | /movies/update/{movie_id} |
| Delete   | DELETE | /movies/{movie_id}        |

### Showtimes
| Action   | Method | Endpoint                        |
|----------|--------|---------------------------------|
| Get      | GET    | /showtimes/{showtime_id}        |
| Get All  | GET    | /showtimes                      |
| Create   | POST   | /showtimes                      |
| Update   | PUT    | /showtimes/update/{showtime_id} |
| Delete   | DELETE | /showtimes/{showtime_id}        |

### Bookings
| Action   | Method | Endpoint               |
|----------|--------|------------------------|
| Get      | GET    | /bookings/{booking_id} |
| Get All  | GET    | /bookings              |
| Create   | POST   | /bookings              |
| Delete   | DELETE | /bookings/{booking_id} |

### Customers
| Action         | Method | Endpoint                          |
|----------------|--------|-----------------------------------|
| Get            | GET    | /customers/{customer_id}          |
| Get All        | GET    | /customers                        |
| View Bookings  | GET    | /customers/{customer_id}/bookings |
| Create         | POST   | /customers                        |
| Delete         | DELETE | /customers/{customer_id}          |

## Notes
- A showtime **can** be updated even if it has bookings.
- A customer, movie, showtime **cannot** be deleted if they have existing bookings.
- Booking a seat that's already taken returns a `409 Conflict`.
- Validation is enforced for fields like names, emails, prices, and times.

## Utilities
- `docker-compose.yml` - launches MySQL with appropriate credentials
- `schema.sql` – optional fallback, but data is injected via `@PostConstruct` loaders

## Database Configuration (MySQL)
- **Host:** localhost  
- **Port:** 3306  
- **Database name:** `db`  
- **Username:** `admin`  
- **Password:** `admin123` 

## Contact
Developed by: Yair Fabri  
Email: yfabri7@gmail.com
If you have questions about the implementation, feel free to reach out!
