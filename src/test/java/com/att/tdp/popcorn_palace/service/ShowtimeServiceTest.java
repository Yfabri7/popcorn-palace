// File: src/test/java/com/att/tdp/popcorn_palace/service/ShowtimeServiceTest.java
package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.exception.ShowtimeConflictException;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.impl.ShowtimeServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private TicketBookingRepository bookingRepository;

    @InjectMocks
    private ShowtimeServiceImpl showtimeService;

    private Showtime showtime;
    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie();
        movie.setId(1L);
        movie.setDuration(120);

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheater("Theater A");
        showtime.setStartTime(LocalDateTime.of(2025, 4, 8, 14, 0));
        showtime.setEndTime(showtime.getStartTime().plusMinutes(movie.getDuration()));
        showtime.setPrice(BigDecimal.valueOf(45));
    }

    @Test
    @DisplayName("Create showtime when no conflict")
    void createShowtime_success() {
        when(showtimeRepository.existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(any(), any(), any()))
                .thenReturn(false);
        when(showtimeRepository.save(any())).thenReturn(showtime);

        Showtime result = showtimeService.createShowtime(showtime);

        assertNotNull(result);
        verify(showtimeRepository).save(any());
    }

    @Test
    @DisplayName("Create showtime with overlap should throw")
    void createShowtime_conflict_shouldThrow() {
        when(showtimeRepository.existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(any(), any(), any()))
                .thenReturn(true);

        assertThrows(ShowtimeConflictException.class, () -> showtimeService.createShowtime(showtime));
    }

    @Test
    @DisplayName("Get existing showtime by ID")
    void getShowtime_success() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));

        Showtime result = showtimeService.getShowtime(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("Get non-existent showtime should throw")
    void getShowtime_notFound_shouldThrow() {
        when(showtimeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> showtimeService.getShowtime(999L));
    }

    @Test
    @DisplayName("Delete showtime with no bookings should succeed")
    void deleteShowtime_noBookings_shouldSucceed() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(bookingRepository.existsByShowtime(showtime)).thenReturn(false);

        showtimeService.deleteShowtime(1L);

        verify(showtimeRepository).delete(showtime);
    }

    @Test
    @DisplayName("Delete showtime with existing bookings should throw")
    void deleteShowtime_withBookings_shouldThrow() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(bookingRepository.existsByShowtime(showtime)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> showtimeService.deleteShowtime(1L));
    }

    @Test
    @DisplayName("Update showtime with valid data")
    void updateShowtime_success() {
        Showtime updated = new Showtime();
        updated.setId(1L);
        updated.setMovie(movie);
        updated.setTheater("Theater B");
        updated.setStartTime(LocalDateTime.of(2025, 4, 8, 18, 0));
        updated.setPrice(BigDecimal.valueOf(55));

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(showtimeRepository.existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                any(), any(), any(), anyLong()))
                .thenReturn(false);
        when(showtimeRepository.save(any())).thenReturn(updated);

        Showtime result = showtimeService.updateShowtime(1L, updated);

        assertEquals("Theater B", result.getTheater());
        assertEquals(BigDecimal.valueOf(55), result.getPrice());
    }

    @Test
    @DisplayName("Update showtime with overlap should throw")
    void updateShowtime_overlap_shouldThrow() {
        Showtime updated = new Showtime();
        updated.setId(1L);
        updated.setMovie(movie);
        updated.setTheater("Theater A");
        updated.setStartTime(LocalDateTime.of(2025, 4, 8, 14, 30));
        updated.setPrice(BigDecimal.valueOf(45));

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(showtimeRepository.existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                any(), any(), any(), anyLong()))
                .thenReturn(true);

        assertThrows(ShowtimeConflictException.class, () -> showtimeService.updateShowtime(1L, updated));
    }

    @Test
    @DisplayName("Get all showtimes returns empty list")
    void getAllShowtimes_empty() {
        when(showtimeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Showtime> result = showtimeService.getAllShowtimes();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Get all showtimes returns populated list")
    void getAllShowtimes_nonEmpty() {
        when(showtimeRepository.findAll()).thenReturn(List.of(showtime));

        List<Showtime> result = showtimeService.getAllShowtimes();

        assertEquals(1, result.size());
    }
}
