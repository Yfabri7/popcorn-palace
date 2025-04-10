package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;

import java.util.List;

public interface TicketBookingService {
    TicketBookingResponse bookTicket(TicketBookingRequest booking);
    List<TicketBookingResponse> getAllBookings();
    TicketBookingResponse getBooking(Long id);
    void cancelBooking(Long id);
}
