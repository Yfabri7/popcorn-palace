package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.TicketBooking;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.TicketBookingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketBookingServiceImpl implements TicketBookingService {

    private final TicketBookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final CustomerRepository customerRepository;

    public TicketBookingServiceImpl(
        TicketBookingRepository bookingRepository,
        ShowtimeRepository showtimeRepository,
        CustomerRepository customerRepository) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public TicketBookingResponse bookTicket(TicketBookingRequest request) {
        Showtime showtime = showtimeRepository.findById(request.getShowtimeId())
                .orElseThrow(() -> new EntityNotFoundException("Showtime not found"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        boolean isBooked = bookingRepository.existsByShowtimeAndSeatNumber(showtime, request.getSeatNumber());
        if (isBooked) {
            throw new SeatAlreadyBookedException("Seat already booked for this showtime");
        }

        TicketBooking booking = request.toEntity(showtime, customer);
        booking.setBookedAt(LocalDateTime.now());

        TicketBooking savedBooking = bookingRepository.save(booking);
        return TicketBookingResponse.fromEntity(savedBooking);
    }

    @Override
    public List<TicketBookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(TicketBookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TicketBookingResponse getBooking(Long id) {
        TicketBooking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id + " not found"));
        return TicketBookingResponse.fromEntity(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EntityNotFoundException("Booking with id " + id + " not found");
        }
        bookingRepository.deleteById(id);
    }
}
