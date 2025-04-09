// File: src/main/java/com/att/tdp/popcorn_palace/repository/TicketBookingRepository.java
package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.TicketBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketBookingRepository extends JpaRepository<TicketBooking, Long> {
    boolean existsByShowtime(Showtime showtime);
    boolean existsByCustomer(Customer customer);
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, Integer seatNumber);
}
