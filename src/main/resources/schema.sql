DROP TABLE IF EXISTS ticket_booking;
DROP TABLE IF EXISTS showtime;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS customer;

CREATE TABLE movie (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    genre VARCHAR(30) NOT NULL,
    duration INT NOT NULL,
    rating DOUBLE NOT NULL,
    release_year INT NOT NULL
);

CREATE TABLE customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE showtime (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    movie_id BIGINT NOT NULL,
    theater VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (movie_id) REFERENCES movie(id)
);

CREATE TABLE ticket_booking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    showtime_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    seat_number INT NOT NULL,
    booked_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (showtime_id) REFERENCES showtime(id),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);
