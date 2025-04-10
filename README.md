🎬 Popcorn Palace – Movie Ticket Booking System
Welcome to Popcorn Palace, a RESTful backend system built with Spring Boot for managing movie ticket bookings. This system allows users to browse movies, manage showtimes, and reserve seats at the theater — all via clean and structured API endpoints.

📌 Project Features
This API enables:

🎞 Movie Management
Add new movies (title, genre, duration, rating, release year)

Update existing movie details

Delete movies (if no showtimes are associated)

Retrieve all available movies

Retrieve specific movie by ID

🕒 Showtime Scheduling
Schedule new showtimes for movies

Prevent overlapping showtimes per theater

Update or delete showtimes

Fetch showtime details by ID

Calculate endTime automatically based on start time + movie duration

🎟 Ticket Booking
Book a seat for a showtime

Enforce seat uniqueness per showtime

Prevent deletion of customers or movies if dependent entities exist

👤 Customer Management
Register new customers

Fetch all customers or a specific customer by ID

Delete customers only if they have no active bookings

View a customer's booking history

✅ Validation & Error Handling
All API requests are validated using jakarta.validation

Custom exceptions ensure meaningful error messages

Global exception handler returns user-friendly messages and appropriate HTTP status codes

🔬 Testing Coverage
Full unit and integration testing using MockMvc and Spring Boot's test utilities

Edge cases are covered for all services and controllers

Profile "test" used for in-memory H2 DB testing

🚀 Get Started
To run, build, and test the application, see the full instructions in Instructions.md
