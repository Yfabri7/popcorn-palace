package com.att.tdp.popcorn_palace.bootstrap;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.Showtime;
import com.att.tdp.popcorn_palace.model.TicketBooking;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.repository.ShowtimeRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TicketBookingLoader implements ApplicationListener<ApplicationReadyEvent> {

    private final TicketBookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final CustomerRepository customerRepository;

    public TicketBookingLoader(
        TicketBookingRepository bookingRepository,
        ShowtimeRepository showtimeRepository,
        CustomerRepository customerRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (bookingRepository.count() > 0) return;

        List<Showtime> showtimes = showtimeRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        if (showtimes.isEmpty() || customers.isEmpty()) return;

        for (int i = 0; i < Math.min(showtimes.size(), customers.size()); i++) {
            Showtime showtime = showtimes.get(i);
            Customer customer = customers.get(i);

            TicketBooking booking = new TicketBooking();
            booking.setShowtime(showtime);
            booking.setCustomer(customer);
            booking.setSeatNumber(10 + i);

            bookingRepository.save(booking);
        }
    }
}
