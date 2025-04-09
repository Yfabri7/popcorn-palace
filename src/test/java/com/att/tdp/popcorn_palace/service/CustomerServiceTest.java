// File: src/test/java/com/att/tdp/popcorn_palace/service/CustomerServiceTest.java
package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.repository.TicketBookingRepository;
import com.att.tdp.popcorn_palace.service.impl.CustomerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private TicketBookingRepository bookingRepository;
    private CustomerServiceImpl service;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        bookingRepository = mock(TicketBookingRepository.class);
        service = new CustomerServiceImpl(customerRepository, bookingRepository);

        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice@example.com");
    }

    @Test
    @DisplayName("createCustomer - should return created customer")
    void createCustomer_shouldReturnCustomer() {
        when(customerRepository.save(any())).thenReturn(customer);

        Customer result = service.createCustomer(customer);

        assertNotNull(result);
        assertEquals("Alice Johnson", result.getFullName());
        assertEquals("alice@example.com", result.getEmail());
    }

    @Test
    @DisplayName("getAllCustomers - should return list")
    void getAllCustomers_shouldReturnList() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));

        List<Customer> result = service.getAllCustomers();

        assertEquals(1, result.size());
        assertEquals("Alice Johnson", result.get(0).getFullName());
    }

    @Test
    @DisplayName("deleteCustomer - no bookings should succeed")
    void deleteCustomer_noBookings_shouldSucceed() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.existsByCustomer(customer)).thenReturn(false);

        service.deleteCustomer(1L);

        verify(customerRepository).delete(customer);
    }

    @Test
    @DisplayName("deleteCustomer - customer has bookings should throw")
    void deleteCustomer_hasBookings_shouldThrow() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bookingRepository.existsByCustomer(customer)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.deleteCustomer(1L));
    }

    @Test
    @DisplayName("deleteCustomer - customer not found should throw")
    void deleteCustomer_notFound_shouldThrow() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.deleteCustomer(99L));
    }
}
