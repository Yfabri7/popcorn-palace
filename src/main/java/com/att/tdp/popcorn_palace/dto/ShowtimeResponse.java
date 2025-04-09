// File: src/main/java/com/att/tdp/popcorn_palace/dto/ShowtimeResponse.java
package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.Showtime;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ShowtimeResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("movieId")
    private Long movieId;

    @JsonProperty("theater")
    private String theater;

    @JsonProperty("startTime")
    private LocalDateTime startTime;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("price")
    private BigDecimal price;

    public static ShowtimeResponse fromEntity(Showtime showtime) {
        ShowtimeResponse response = new ShowtimeResponse();
        response.id = showtime.getId();
        response.movieId = showtime.getMovie() != null ? showtime.getMovie().getId() : null;
        response.theater = showtime.getTheater();
        response.startTime = showtime.getStartTime();
        response.endTime = showtime.getEndTime();
        response.price = showtime.getPrice();
        return response;
    }

    public Long getId() { return id; }
    public Long getMovieId() { return movieId; }
    public String getTheater() { return theater; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public BigDecimal getPrice() { return price; }
}
