package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.MovieCreateRequest;
import com.att.tdp.popcorn_palace.dto.MovieUpdateRequest;
import com.att.tdp.popcorn_palace.dto.MovieResponse;
import com.att.tdp.popcorn_palace.exception.DuplicateMovieException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.service.impl.MovieServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @InjectMocks
    private MovieServiceImpl movieService;

    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setGenre("Action");
        movie.setDuration(120);
        movie.setRating(8.5);
        movie.setReleaseYear(2022);
    }

    @Test
    @DisplayName("createMovie should return response")
    void createMovie_ShouldReturnResponse() {
        MovieCreateRequest request = new MovieCreateRequest();
        request.setTitle("Test Movie");
        request.setGenre("Action");
        request.setDuration(120);
        request.setRating(8.5);
        request.setReleaseYear(2022);

        when(movieRepository.findByTitleAndGenreAndReleaseYear("Test Movie", "Action", 2022)).thenReturn(null);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        MovieResponse result = movieService.createMovie(request);

        assertNotNull(result);
        assertEquals("Test Movie", result.getTitle());
    }

    @Test
    @DisplayName("createMovie should throw DuplicateMovieException")
    void createMovie_ShouldThrowDuplicateException() {
        MovieCreateRequest request = new MovieCreateRequest();
        request.setTitle("Test Movie");
        request.setGenre("Action");
        request.setDuration(120);
        request.setRating(8.5);
        request.setReleaseYear(2022);

        when(movieRepository.findByTitleAndGenreAndReleaseYear("Test Movie", "Action", 2022)).thenReturn(movie);

        assertThrows(DuplicateMovieException.class, () -> movieService.createMovie(request));
    }

    @Test
    @DisplayName("getAllMovies should return list of responses")
    void getAllMovies_ShouldReturnResponses() {
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<MovieResponse> result = movieService.getAllMovies();

        assertEquals(1, result.size());
        assertEquals("Test Movie", result.get(0).getTitle());
    }

    @Test
    @DisplayName("updateMovie should return updated response")
    void updateMovie_ShouldReturnUpdatedResponse() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(any())).thenReturn(movie);

        MovieUpdateRequest updateRequest = new MovieUpdateRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setGenre("Drama");
        updateRequest.setRating(7.8);

        MovieResponse result = movieService.updateMovie(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Drama", result.getGenre());
        assertEquals(7.8, result.getRating());
    }

    @Test
    @DisplayName("updateMovie should throw when movie not found")
    void updateMovie_NotFound_ShouldThrow() {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());

        MovieUpdateRequest updateRequest = new MovieUpdateRequest();
        updateRequest.setTitle("New Title");

        assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(99L, updateRequest));
    }

    @Test
    @DisplayName("deleteMovie should succeed if no showtimes exist")
    void deleteMovie_ShouldSucceed() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.findByMovie(movie)).thenReturn(Collections.emptyList());

        movieService.deleteMovie(1L);

        verify(movieRepository).delete(movie);
    }

    @Test
    @DisplayName("deleteMovie should throw if showtimes exist")
    void deleteMovie_WithShowtimes_ShouldThrow() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(showtimeRepository.findByMovie(movie)).thenReturn(List.of(mock(Showtime.class)));

        assertThrows(IllegalStateException.class, () -> movieService.deleteMovie(1L));
    }

    @Test
    @DisplayName("deleteMovie should throw if movie not found")
    void deleteMovie_NotFound_ShouldThrow() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> movieService.deleteMovie(999L));
    }
}
