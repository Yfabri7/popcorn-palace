package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.ShowtimeRequest;
import com.att.tdp.popcorn_palace.dto.ShowtimeResponse;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/showtimes")
public class ShowtimeController {

    private final ShowtimeService service;
    private final MovieRepository movieRepository;

    public ShowtimeController(ShowtimeService service, MovieRepository movieRepository) {
        this.service = service;
        this.movieRepository = movieRepository;
    }

    @PostMapping
    public ResponseEntity<ShowtimeResponse> createShowtime(@Valid @RequestBody ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
            .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + request.getMovieId()));

        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setStartTime(request.getStartTime());
        showtime.setEndTime(request.getStartTime().plusMinutes(movie.getDuration()));
        showtime.setTheater(request.getTheater());
        showtime.setPrice(request.getPrice());

        Showtime created = service.createShowtime(showtime);
        return ResponseEntity.ok(ShowtimeResponse.fromEntity(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> getShowtime(@PathVariable Long id) {
        Showtime found = service.getShowtime(id);
        return ResponseEntity.ok(ShowtimeResponse.fromEntity(found));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeResponse> updateShowtime(
            @PathVariable Long id,
            @Valid @RequestBody ShowtimeRequest request) {
        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + request.getMovieId()));
        Showtime updated = service.updateShowtime(id, request.toEntity(movie));
        return ResponseEntity.ok(ShowtimeResponse.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        service.deleteShowtime(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ShowtimeResponse>> getAllShowtimes() {
        List<ShowtimeResponse> responses = service.getAllShowtimes().stream()
                .map(ShowtimeResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
