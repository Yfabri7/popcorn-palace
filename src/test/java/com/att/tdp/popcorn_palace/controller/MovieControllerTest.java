package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.MovieCreateRequest;
import com.att.tdp.popcorn_palace.dto.MovieUpdateRequest;
import com.att.tdp.popcorn_palace.dto.MovieResponse;
import com.att.tdp.popcorn_palace.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@ActiveProfiles("test")
@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    private MovieCreateRequest createRequest;
    private MovieUpdateRequest updateRequest;
    private MovieResponse movieResponse;

    @BeforeEach
    void setUp() {
        createRequest = new MovieCreateRequest();
        createRequest.setTitle("Inception");
        createRequest.setGenre("Sci Fi");
        createRequest.setDuration(148);
        createRequest.setRating(8.8);
        createRequest.setReleaseYear(2010);

        updateRequest = new MovieUpdateRequest();
        updateRequest.setTitle("Inception");
        updateRequest.setGenre("Sci Fi");
        updateRequest.setRating(8.8);

        movieResponse = new MovieResponse();
        movieResponse.setId(1L);
        movieResponse.setTitle("Inception");
        movieResponse.setGenre("Sci Fi");
        movieResponse.setDuration(148);
        movieResponse.setRating(8.8);
        movieResponse.setReleaseYear(2010);
    }

    @Test
    @DisplayName("POST /movies - success")
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        when(movieService.createMovie(any(MovieCreateRequest.class))).thenReturn(movieResponse);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test
    @DisplayName("GET /movies - success")
    void getAllMovies_ShouldReturnMovieList() throws Exception {
        when(movieService.getAllMovies()).thenReturn(Collections.singletonList(movieResponse));

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Inception")));
    }

    @Test
    @DisplayName("PUT /movies/{id} - update movie")
    void updateMovie_ShouldReturnUpdatedMovie() throws Exception {
        when(movieService.updateMovie(eq(1L), any(MovieUpdateRequest.class))).thenReturn(movieResponse);

        mockMvc.perform(put("/movies/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Inception")));
    }

    @Test
    @DisplayName("DELETE /movies/{id} - delete movie")
    void deleteMovie_ShouldReturnNoContent() throws Exception {
        doNothing().when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/movies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("PUT /movies/{id} - not found")
    void updateMovie_NotFound_ShouldReturn404() throws Exception {
        when(movieService.updateMovie(eq(999L), any(MovieUpdateRequest.class)))
                .thenThrow(new EntityNotFoundException("Movie not found"));

        mockMvc.perform(put("/movies/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /movies - missing genre")
    void createMovie_missingGenre_shouldReturnBadRequest() throws Exception {
        createRequest.setGenre(null);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /movies - invalid genre")
    void createMovie_invalidGenre_shouldReturnBadRequest() throws Exception {
        createRequest.setGenre("123");

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /movies - missing title")
    void createMovie_missingTitle_shouldReturnBadRequest() throws Exception {
        createRequest.setTitle(null);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /movies - invalid rating")
    void createMovie_invalidRating_shouldReturnBadRequest() throws Exception {
        createRequest.setRating(11.0);

        mockMvc.perform(post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }
}
