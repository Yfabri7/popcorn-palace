package com.att.tdp.popcorn_palace.bootstrap;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MovieLoader {

    private final MovieRepository movieRepository;

    public MovieLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void loadMovies() {
        if (movieRepository.count() == 0) {
            Movie inception = new Movie();
            inception.setTitle("Inception");
            inception.setGenre("Sci Fi");
            inception.setDuration(148);
            inception.setRating(8.8);
            inception.setReleaseYear(2010);

            Movie matrix = new Movie();
            matrix.setTitle("The Matrix");
            matrix.setGenre("Action");
            matrix.setDuration(136);
            matrix.setRating(8.7);
            matrix.setReleaseYear(1999);

            movieRepository.save(inception);
            movieRepository.save(matrix);
        }
    }
}
