package com.att.tdp.popcorn_palace.service.impl;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.model.TicketBooking;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TicketBookingRepository bookingRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository, TicketBookingRepository bookingRepository) {
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));

        boolean hasBookings = bookingRepository.existsByCustomer(customer);
        if (hasBookings) {
            throw new IllegalStateException("Cannot delete customer with existing ticket bookings.");
        }

        customerRepository.delete(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer with id " + id + " not found"));
    }

    @Override
    public List<TicketBookingResponse> getBookingsByCustomerId(Long customerId) {
        Customer customer = getCustomerById(customerId);
        List<TicketBooking> bookings = customer.getBookings();
        return bookings.stream()
                .map(TicketBookingResponse::fromEntity)
                .collect(Collectors.toList());
    }
}
