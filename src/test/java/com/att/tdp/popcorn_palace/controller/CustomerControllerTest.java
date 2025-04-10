package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.CustomerRequest;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
@SpringJUnitConfig
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private CustomerService customerService;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerRequest customerRequest;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerRequest = new CustomerRequest();
        customerRequest.setFullName("Alice Johnson");
        customerRequest.setEmail("alice@example.com");

        customer = new Customer();
        customer.setId(1L);
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice@example.com");
    }

    @Test
    @DisplayName("POST /customers - should return created customer")
    void createCustomer_shouldReturnCreated() throws Exception {
        when(customerService.createCustomer(Mockito.any(Customer.class))).thenAnswer(invocation -> {
            Customer input = invocation.getArgument(0);
            input.setId(1L);
            return input;
        });

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Alice Johnson")))
                .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    @Test
    @DisplayName("GET /customers - should return all customers")
    void getAllCustomers_shouldReturnList() throws Exception {
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].fullName", is("Alice Johnson")))
                .andExpect(jsonPath("$[0].email", is("alice@example.com")));
    }

    @Test
    @DisplayName("GET /customers/{id} - valid ID should return customer")
    void getCustomerById_valid_shouldReturnCustomer() throws Exception {
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.fullName", is("Alice Johnson")))
                .andExpect(jsonPath("$.email", is("alice@example.com")));
    }

    @Test
    @DisplayName("GET /customers/{id} - non-existent ID should return 404")
    void getCustomerById_notFound_shouldReturn404() throws Exception {
        when(customerService.getCustomerById(99L)).thenThrow(new EntityNotFoundException("Customer not found with id: 99"));

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /customers/{id} - valid ID should delete")
    void deleteCustomer_valid_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    @DisplayName("DELETE /customers/{id} - not found should return 404")
    void deleteCustomer_notFound_shouldReturnNotFound() throws Exception {
        doThrow(new EntityNotFoundException("Customer not found"))
                .when(customerService).deleteCustomer(99L);

        mockMvc.perform(delete("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /customers - missing full name should return 400")
    void createCustomer_missingFullName_shouldReturnBadRequest() throws Exception {
        customerRequest.setFullName("");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /customers - invalid email format should return 400")
    void createCustomer_invalidEmail_shouldReturnBadRequest() throws Exception {
        customerRequest.setEmail("not-an-email");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /customers/{id}/bookings - should return bookings for customer")
    void getBookingsForCustomer_shouldReturnList() throws Exception {
        TicketBookingResponse booking = new TicketBookingResponse();
        booking.setId(10L);
        booking.setSeatNumber(5);
        booking.setTheater("Theater A");
        booking.setPrice(new BigDecimal("42.00"));
        booking.setStartTime(LocalDateTime.now());
        booking.setEndTime(LocalDateTime.now().plusHours(2));
        booking.setMovieTitle("Dune Part Two");

        when(customerService.getBookingsByCustomerId(1L)).thenReturn(List.of(booking));

        mockMvc.perform(get("/customers/1/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].bookingId", is(10)))
                .andExpect(jsonPath("$[0].seatNumber", is(5)))
                .andExpect(jsonPath("$[0].theater", is("Theater A")))
                .andExpect(jsonPath("$[0].price", is(42.00)))
                .andExpect(jsonPath("$[0].movieTitle", is("Dune Part Two")));
    }
}
