package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.MovieCreateRequest;
import com.att.tdp.popcorn_palace.dto.MovieUpdateRequest;
import com.att.tdp.popcorn_palace.dto.MovieResponse;
import com.att.tdp.popcorn_palace.exception.DuplicateMovieException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieServiceImpl(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public MovieResponse createMovie(MovieCreateRequest request) {
        Movie existingMovie = movieRepository.findByTitleAndGenreAndReleaseYear(
            request.getTitle(), request.getGenre(), request.getReleaseYear());

        if (existingMovie != null) {
            throw new DuplicateMovieException("Movie with same title, genre, and release year already exists.");
        }

        Movie movie = new Movie();
        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setDuration(request.getDuration());
        movie.setRating(request.getRating());
        movie.setReleaseYear(request.getReleaseYear());

        return mapToResponse(movieRepository.save(movie));
    }

    @Override
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public MovieResponse updateMovie(Long id, MovieUpdateRequest request) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Movie not found with id " + id));

        movie.setTitle(request.getTitle());
        movie.setGenre(request.getGenre());
        movie.setRating(request.getRating());

        return mapToResponse(movieRepository.save(movie));
    }

    @Override
    public void deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        List<Showtime> showtimes = showtimeRepository.findByMovie(movie);
        if (!showtimes.isEmpty()) {
            throw new IllegalStateException("Cannot delete movie with scheduled showtimes. Cancel the showtimes before deleting the movie.");
        }

        movieRepository.delete(movie);
    }

    private MovieResponse mapToResponse(Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setGenre(movie.getGenre());
        response.setDuration(movie.getDuration());
        response.setRating(movie.getRating());
        response.setReleaseYear(movie.getReleaseYear());
        return response;
    }
}

