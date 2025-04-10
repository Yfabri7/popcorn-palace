package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.TicketBooking;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class TicketBookingRequest {

    @NotNull
    private Long showtimeId;

    @NotNull(message = "Seat number is required")
    @Min(value = 1, message = "Seat number must be at least 1")
    @Max(value = 100, message = "Seat number must be at most 100")
    private Integer seatNumber;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    public Long getShowtimeId() {
        return showtimeId;
    }

    public void setShowtimeId(Long showtimeId) {
        this.showtimeId = showtimeId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public TicketBooking toEntity(Showtime showtime, Customer customer) {
        TicketBooking booking = new TicketBooking();
        booking.setShowtime(showtime);
        booking.setCustomer(customer);
        booking.setSeatNumber(seatNumber);
        return booking;
    }
}
