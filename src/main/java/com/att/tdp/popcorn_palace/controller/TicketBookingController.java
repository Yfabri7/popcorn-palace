// File: src/main/java/com/att/tdp/popcorn_palace/controller/TicketBookingController.java
package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.TicketBookingRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.service.TicketBookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/bookings")
public class TicketBookingController {

    private final TicketBookingService service;

    public TicketBookingController(TicketBookingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TicketBookingResponse> bookTicket(@Valid @RequestBody TicketBookingRequest request) {
        TicketBookingResponse response = service.bookTicket(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TicketBookingResponse>> getAllBookings() {
        List<TicketBookingResponse> responses = service.getAllBookings();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketBookingResponse> getBooking(@PathVariable Long id) {
        return ResponseEntity.ok(service.getBooking(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(@PathVariable Long id) {
        service.cancelBooking(id);
    }
}
