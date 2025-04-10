package com.att.tdp.popcorn_palace.dto;

import com.att.tdp.popcorn_palace.model.TicketBooking;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketBookingResponse {

    @JsonProperty("bookingId")
    private Long bookingId;

    @JsonProperty("showtimeId")
    private Long showtimeId;

    @JsonProperty("seatNumber")
    private Integer seatNumber;

    @JsonProperty("customerId")
    private Long customerId;

    @JsonProperty("customerName")
    private String customerName;

    @JsonProperty("bookedAt")
    private LocalDateTime bookedAt;

    private String theater;
    private BigDecimal price;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String movieTitle;

    public TicketBookingResponse() {
    }

    public TicketBookingResponse(Long bookingId, Long showtimeId, Integer seatNumber,
                                 Long customerId, String customerName, LocalDateTime bookedAt) {
        this.bookingId = bookingId;
        this.showtimeId = showtimeId;
        this.seatNumber = seatNumber;
        this.customerId = customerId;
        this.customerName = customerName;
        this.bookedAt = bookedAt;
    }

    public static TicketBookingResponse fromEntity(TicketBooking booking) {
        TicketBookingResponse response = new TicketBookingResponse(
                booking.getId(),
                booking.getShowtime().getId(),
                booking.getSeatNumber(),
                booking.getCustomer().getId(),
                booking.getCustomer().getFullName(),
                booking.getBookedAt()
        );

        if (booking.getShowtime() != null) {
            response.setTheater(booking.getShowtime().getTheater());
            response.setPrice(booking.getShowtime().getPrice());
            response.setStartTime(booking.getShowtime().getStartTime());
            response.setEndTime(booking.getShowtime().getEndTime());
            response.setMovieTitle(booking.getShowtime().getMovie().getTitle());
        }

        return response;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getBookedAt() {
        return bookedAt;
    }

    public void setBookedAt(LocalDateTime bookedAt) {
        this.bookedAt = bookedAt;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setId(Long id) {
        this.bookingId = id;
    }
}
