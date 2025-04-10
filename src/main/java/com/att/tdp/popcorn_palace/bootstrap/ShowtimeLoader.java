package com.att.tdp.popcorn_palace.bootstrap;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ShowtimeLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public ShowtimeLoader(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<Movie> movies = movieRepository.findAll();
        int theaterCounter = 0;

        for (Movie movie : movies) {
            if (!showtimeRepository.findByMovie(movie).isEmpty()) continue;

            Showtime showtime = new Showtime();
            showtime.setMovie(movie);
            showtime.setTheater("Theater " + (char) ('A' + theaterCounter));
            showtime.setStartTime(LocalDateTime.of(2025, 4, 8, 12 + 2 * theaterCounter, 0));
            showtime.setEndTime(showtime.getStartTime().plusMinutes(movie.getDuration()));
            showtime.setPrice(BigDecimal.valueOf(30 + 5 * theaterCounter));

            showtimeRepository.save(showtime);
            theaterCounter++;
        }
    }
}
