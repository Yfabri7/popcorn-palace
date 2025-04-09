// File: src/main/java/com/att/tdp/popcorn_palace/repository/MovieRepository.java
package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByTitleAndGenreAndReleaseYear(String title, String genre, int releaseYear);
}