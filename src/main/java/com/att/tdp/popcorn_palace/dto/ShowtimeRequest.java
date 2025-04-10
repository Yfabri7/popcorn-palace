package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowtimeRequest {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotBlank(message = "Theater name is required")
    @Size(min = 2, max = 50, message = "Theater name must be between 2 and 50 characters")
    private String theater;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private LocalDateTime startTime;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be at least 0.01")
    @Digits(integer = 6, fraction = 2, message = "Price must have up to 2 decimal places and max 6 digits")
    private BigDecimal price;

    public Showtime toEntity(Movie movie) {
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(this.theater);
        showtime.setStartTime(this.startTime);
        showtime.setEndTime(this.startTime.plusMinutes(movie.getDuration()));
        showtime.setPrice(this.price);
        return showtime;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
