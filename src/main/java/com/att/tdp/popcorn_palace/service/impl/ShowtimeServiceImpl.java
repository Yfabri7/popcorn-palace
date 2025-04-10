package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.exception.ShowtimeConflictException;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.ShowtimeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.att.tdp.popcorn_palace.repository.MovieRepository;

import java.util.List;

@Service
public class ShowtimeServiceImpl implements ShowtimeService {

    private final ShowtimeRepository showtimeRepository;
    private final TicketBookingRepository bookingRepository;

    public ShowtimeServiceImpl(
        TicketBookingRepository bookingRepository,
        ShowtimeRepository showtimeRepository,
        MovieRepository movieRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public Showtime createShowtime(Showtime showtime) {
        if (showtime.getMovie() == null || showtime.getMovie().getDuration() == 0) {
            throw new IllegalArgumentException("Movie and its duration must be provided");
        }

        if (showtime.getStartTime() == null || showtime.getTheater() == null) {
            throw new IllegalArgumentException("StartTime and Theater are required");
        }

        showtime.setEndTime(showtime.getStartTime().plusMinutes(showtime.getMovie().getDuration()));

        boolean overlaps = showtimeRepository.existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            showtime.getTheater(), showtime.getEndTime(), showtime.getStartTime());

        if (overlaps) {
            throw new ShowtimeConflictException("Overlapping showtime at this theater");
        }

        return showtimeRepository.save(showtime);
    }

    @Override
    public Showtime getShowtime(Long id) {
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Showtime not found with id: " + id));
    }

    @Override
    public Showtime updateShowtime(Long id, Showtime updated) {
        Showtime existing = getShowtime(id);

        if (updated.getMovie() == null) {
            throw new IllegalArgumentException("Movie must be provided for the showtime");
        }

        boolean overlaps = showtimeRepository
            .existsByTheaterAndStartTimeLessThanEqualAndEndTimeGreaterThanEqualAndIdNot(
                updated.getTheater(), 
                updated.getStartTime().plusMinutes(updated.getMovie().getDuration()),
                updated.getStartTime(),
                id);

        if (overlaps) {
            throw new ShowtimeConflictException("Overlapping showtime at this theater");
        }

        existing.setMovie(updated.getMovie());
        existing.setTheater(updated.getTheater());
        existing.setStartTime(updated.getStartTime());
        existing.setEndTime(updated.getStartTime().plusMinutes(updated.getMovie().getDuration()));
        existing.setPrice(updated.getPrice());

        return showtimeRepository.save(existing);
    }

    @Override
    public void deleteShowtime(Long id) {
        Showtime showtime = showtimeRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Showtime not found"));
    
        boolean hasBookings = bookingRepository.existsByShowtime(showtime);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete showtime with existing ticket bookings. Cancle the booking before deleting showtime.");
        }
    
        showtimeRepository.delete(showtime);
    }
    
    @Override
    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }
}
