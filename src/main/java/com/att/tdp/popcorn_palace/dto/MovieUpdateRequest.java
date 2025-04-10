package com.att.tdp.popcorn_palace.dto;

import jakarta.validation.constraints.*;

public class MovieUpdateRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;

    @NotBlank(message = "Genre is required")
    @Size(min = 3, max = 30, message = "Genre must be at least 3 characters long")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Genre must contain only English letters and spaces")
    private String genre;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0.0")
    @DecimalMax(value = "10.0", message = "Rating must be no more than 10.0")
    private double rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
