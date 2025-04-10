package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.TicketBooking;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.impl.TicketBookingServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TicketBookingServiceTest {

    @Mock
    private TicketBookingRepository bookingRepository;

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private TicketBookingServiceImpl service;

    private TicketBookingRequest request;
    private TicketBooking booking;
    private Showtime showtime;
    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setDuration(120);

        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("John Doe");

        showtime = new Showtime();
        showtime.setId(1L);
        showtime.setMovie(movie);
        showtime.setTheater("Theater 1");
        showtime.setStartTime(LocalDateTime.now().plusHours(1));
        showtime.setEndTime(showtime.getStartTime().plusMinutes(120));
        showtime.setPrice(BigDecimal.valueOf(30));

        request = new TicketBookingRequest();
        request.setShowtimeId(1L);
        request.setSeatNumber(11);
        request.setCustomerId(1L);

        booking = new TicketBooking();
        booking.setId(1L);
        booking.setShowtime(showtime);
        booking.setCustomer(customer);
        booking.setSeatNumber(11);
    }

    @Test
    @DisplayName("bookTicket - should book and return response")
    void bookTicket_shouldReturnResponse() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.existsByShowtimeAndSeatNumber(showtime, 11)).thenReturn(false);
        when(bookingRepository.save(any(TicketBooking.class))).thenReturn(booking);

        TicketBookingResponse result = service.bookTicket(request);

        assertNotNull(result);
        assertEquals(11, result.getSeatNumber());
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    @DisplayName("bookTicket - seat already booked should throw exception")
    void bookTicket_seatAlreadyBooked_shouldThrow() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.existsByShowtimeAndSeatNumber(showtime, 11)).thenReturn(true);

        assertThrows(SeatAlreadyBookedException.class, () -> service.bookTicket(request));
    }

    @Test
    @DisplayName("bookTicket - showtime not found should throw")
    void bookTicket_missingShowtime_shouldThrow() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.bookTicket(request));
    }

    @Test
    @DisplayName("bookTicket - customer not found should throw")
    void bookTicket_missingCustomer_shouldThrow() {
        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(showtime));
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.bookTicket(request));
    }

    @Test
    @DisplayName("getAllBookings - should return list")
    void getAllBookings_shouldReturnList() {
        when(bookingRepository.findAll()).thenReturn(Collections.singletonList(booking));

        var result = service.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(11, result.get(0).getSeatNumber());
    }

    @Test
    @DisplayName("getAllBookings - empty list should return empty")
    void getAllBookings_empty_shouldReturnEmpty() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        var result = service.getAllBookings();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("getBooking - existing ID should return booking")
    void getBooking_existingId_shouldReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        TicketBookingResponse result = service.getBooking(1L);

        assertEquals(11, result.getSeatNumber());
        assertEquals("John Doe", result.getCustomerName());
    }

    @Test
    @DisplayName("getBooking - non-existent ID should throw")
    void getBooking_nonExistentId_shouldThrow() {
        when(bookingRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.getBooking(999L));
    }

    @Test
    @DisplayName("cancelBooking - existing ID should delete")
    void cancelBooking_existingId_shouldDelete() {
        when(bookingRepository.existsById(1L)).thenReturn(true);

        service.cancelBooking(1L);

        verify(bookingRepository).deleteById(1L);
    }

    @Test
    @DisplayName("cancelBooking - non-existent ID should throw")
    void cancelBooking_nonExistentId_shouldThrow() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> service.cancelBooking(99L));
    }
}
