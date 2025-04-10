package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequest;
import com.att.tdp.popcorn_palace.exception.ShowtimeConflictException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig
@ActiveProfiles("test")
@WebMvcTest(ShowtimeController.class)
class ShowtimeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowtimeService service;

    @MockitoBean
    private MovieRepository movieRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Showtime showtime;
    private ShowtimeRequest showtimeRequest;

    @BeforeEach
    void setup() {
        Movie movie = new Movie();
        movie.setId(1L);

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheater("Hall 1");
        showtime.setStartTime(LocalDateTime.now().plusHours(1));
        showtime.setEndTime(LocalDateTime.now().plusHours(3));
        showtime.setPrice(BigDecimal.TEN);

        showtimeRequest = new ShowtimeRequest();
        showtimeRequest.setMovieId(1L);
        showtimeRequest.setTheater("Hall 1");
        showtimeRequest.setStartTime(showtime.getStartTime());
        showtimeRequest.setPrice(showtime.getPrice());

        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
    }

    @Test
    @DisplayName("POST /showtimes - success")
    void createShowtime_shouldReturnCreated() throws Exception {
        when(service.createShowtime(any())).thenReturn(showtime);

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.theater").value("Hall 1"));
    }

    @Test
    @DisplayName("POST /showtimes - conflict on overlap")
    void createShowtime_shouldReturnConflictOnOverlap() throws Exception {
        when(service.createShowtime(any()))
                .thenThrow(new ShowtimeConflictException("Overlapping showtime at this theater"));

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Overlapping showtime at this theater"));
    }

    @Test
    @DisplayName("POST /showtimes - startTime in the past should return 400")
    void createShowtime_startTimeInPast_shouldReturnBadRequest() throws Exception {
        showtimeRequest.setStartTime(LocalDateTime.now().minusHours(2));

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /showtimes - negative price should return 400")
    void createShowtime_negativePrice_shouldReturnBadRequest() throws Exception {
        showtimeRequest.setPrice(BigDecimal.valueOf(-5));

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /showtimes/{id} - success")
    void getShowtime_shouldReturnOk() throws Exception {
        when(service.getShowtime(1L)).thenReturn(showtime);

        mockMvc.perform(get("/showtimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("PUT /showtimes/{id} - update success")
    void updateShowtime_shouldReturnUpdated() throws Exception {

        showtime.setTheater("Updated Hall");
        when(service.updateShowtime(any(), any())).thenReturn(showtime);

        mockMvc.perform(put("/showtimes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater").value("Updated Hall"));
    }

    @Test
    @DisplayName("DELETE /showtimes/{id} - delete success")
    void deleteShowtime_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/showtimes/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /showtimes - return list")
    void getAllShowtimes_shouldReturnList() throws Exception {
        when(service.getAllShowtimes()).thenReturn(List.of(showtime));

        mockMvc.perform(get("/showtimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("POST /showtimes - missing theater should return 400")
    void createShowtime_missingTheater_shouldReturnBadRequest() throws Exception {
        showtimeRequest.setTheater(null);

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /showtimes - missing price should return 400")
    void createShowtime_missingPrice_shouldReturnBadRequest() throws Exception {
        showtimeRequest.setPrice(null);

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PUT /showtimes/{id} - non-existent showtime should return 404")
    void updateShowtime_notFound_shouldReturnNotFound() throws Exception {
        when(service.updateShowtime(any(), any()))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Showtime not found"));

        mockMvc.perform(put("/showtimes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /showtimes/{id} - non-existent showtime should return 404")
    void deleteShowtime_notFound_shouldReturnNotFound() throws Exception {
        doThrow(new jakarta.persistence.EntityNotFoundException("Showtime not found"))
                .when(service).deleteShowtime(999L);

        mockMvc.perform(delete("/showtimes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /showtimes - empty request body should return 400")
    void createShowtime_emptyBody_shouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /showtimes - movieId not found should return 404")
    void createShowtime_movieNotFound_shouldReturnNotFound() throws Exception {
        when(movieRepository.findById(99L)).thenReturn(Optional.empty());
        showtimeRequest.setMovieId(99L);

        mockMvc.perform(post("/showtimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(showtimeRequest)))
                .andExpect(status().isNotFound());
    }
}
