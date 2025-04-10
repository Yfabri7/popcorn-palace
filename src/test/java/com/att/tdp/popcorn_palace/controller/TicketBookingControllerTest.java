package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.exception.GlobalExceptionHandler;
import com.att.tdp.popcorn_palace.exception.SeatAlreadyBookedException;
import com.att.tdp.popcorn_palace.service.TicketBookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TicketBookingController.class)
@SpringJUnitConfig(TicketBookingController.class)
@ActiveProfiles("test")
@Import(GlobalExceptionHandler.class)
class TicketBookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketBookingService service;

    @Autowired
    private ObjectMapper objectMapper;

    private TicketBookingRequest bookingRequest;
    private TicketBookingResponse bookingResponse;

    @BeforeEach
    void setUp() {
        bookingRequest = new TicketBookingRequest();
        bookingRequest.setSeatNumber(11);
        bookingRequest.setCustomerId(1L);
        bookingRequest.setShowtimeId(1L);

        bookingResponse = new TicketBookingResponse();
        bookingResponse.setBookingId(1L);
        bookingResponse.setSeatNumber(11);
        bookingResponse.setCustomerId(1L);
        bookingResponse.setShowtimeId(1L);
        bookingResponse.setBookedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("POST /bookings - success")
    void bookTicket_ShouldReturnCreated() throws Exception {
        when(service.bookTicket(any(TicketBookingRequest.class))).thenReturn(bookingResponse);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.seatNumber").value(11))
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.showtimeId").value(1));
    }

    @Test
    @DisplayName("GET /bookings - success")
    void getAllBookings_ShouldReturnList() throws Exception {
        when(service.getAllBookings()).thenReturn(List.of(bookingResponse));

        mockMvc.perform(get("/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].seatNumber").value(11))
                .andExpect(jsonPath("$[0].customerId").value(1));
    }

    @Test
    @DisplayName("POST /bookings - missing seatNumber should return 400")
    void bookTicket_missingSeatNumber_shouldReturnBadRequest() throws Exception {
        bookingRequest.setSeatNumber(null);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings - missing customerId should return 400")
    void bookTicket_missingCustomerId_shouldReturnBadRequest() throws Exception {
        bookingRequest.setCustomerId(null);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings - missing showtimeId should return 400")
    void bookTicket_missingShowtimeId_shouldReturnBadRequest() throws Exception {
        bookingRequest.setShowtimeId(null);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings - seat already booked should return 409")
    void bookTicket_seatAlreadyBooked_shouldReturnConflict() throws Exception {
        when(service.bookTicket(any())).thenThrow(new SeatAlreadyBookedException("Seat already booked"));

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("POST /bookings - showtime not found should return 404")
    void bookTicket_showtimeNotFound_shouldReturnNotFound() throws Exception {
        when(service.bookTicket(any())).thenThrow(new EntityNotFoundException("Showtime not found"));

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /bookings/{id} - booking not found should return 404")
    void getBooking_notFound_shouldReturnNotFound() throws Exception {
        when(service.getBooking(999L)).thenThrow(new EntityNotFoundException("Booking not found"));

        mockMvc.perform(get("/bookings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /bookings/{id} - valid id should return booking")
    void getBooking_validId_shouldReturnBooking() throws Exception {
        when(service.getBooking(1L)).thenReturn(bookingResponse);

        mockMvc.perform(get("/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatNumber").value(11))
                .andExpect(jsonPath("$.customerId").value(1));
    }

    @Test
    @DisplayName("DELETE /bookings/{id} - valid id should return no content")
    void cancelBooking_validId_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /bookings/{id} - non-existent booking should return 404")
    void cancelBooking_invalidId_shouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Booking not found"))
                .when(service).cancelBooking(999L);

        mockMvc.perform(delete("/bookings/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /bookings - seat number out of range should return 400")
    void bookTicket_seatNumberOutOfRange_shouldReturnBadRequest() throws Exception {
        bookingRequest.setSeatNumber(101);

        mockMvc.perform(post("/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /bookings - invalid content type should return 415")
    void bookTicket_invalidContentType_shouldReturnUnsupportedMediaType() throws Exception {
        mockMvc.perform(post("/bookings")
                .contentType(MediaType.TEXT_PLAIN)
                .content("invalid data"))
            .andExpect(status().isUnsupportedMediaType());
    }
}
