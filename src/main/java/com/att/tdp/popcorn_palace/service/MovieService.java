// File: src/main/java/com/att/tdp/popcorn_palace/service/MovieService.java
package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieCreateRequest;
import com.att.tdp.popcorn_palace.dto.MovieUpdateRequest;
import com.att.tdp.popcorn_palace.dto.MovieResponse;

import java.util.List;

public interface MovieService {
    MovieResponse createMovie(MovieCreateRequest request);
    List<MovieResponse> getAllMovies();
    MovieResponse updateMovie(Long id, MovieUpdateRequest request);
    void deleteMovie(Long id);
}

