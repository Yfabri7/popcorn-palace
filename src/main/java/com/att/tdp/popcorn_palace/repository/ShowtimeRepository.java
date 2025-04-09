// File: src/main/java/com/att/tdp/popcorn_palace/repository/ShowtimeRepository.java
package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    List<Showtime> findByMovie(Movie movie);
    
    boolean existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
        String theater,
        LocalDateTime endTime,
        LocalDateTime startTime
    );

    boolean existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
        String theater,
        LocalDateTime endTime,
        LocalDateTime startTime,
        Long idToExclude
    );
}
