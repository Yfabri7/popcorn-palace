package com.att.tdp.popcorn_palace.controller;

import com.att.tdp.popcorn_palace.dto.CustomerRequest;
import com.att.tdp.popcorn_palace.dto.CustomerResponse;
import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import com.att.tdp.popcorn_palace.service.CustomerService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;
    private final CustomerService customerService;

    public CustomerController(CustomerRepository repository, CustomerService customerService) {
        this.repository = repository;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFullName(request.getFullName());
        customer.setEmail(request.getEmail());

        Customer saved = repository.save(customer);
        return ResponseEntity.ok(toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAll() {
        List<CustomerResponse> customers = repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        Customer customer = repository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));
        return ResponseEntity.ok(toResponse(customer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/bookings")
    public ResponseEntity<List<TicketBookingResponse>> getBookingsForCustomer(@PathVariable Long id) {
        List<TicketBookingResponse> bookings = customerService.getBookingsByCustomerId(id);
        return ResponseEntity.ok(bookings);
    }

    private CustomerResponse toResponse(Customer customer) {
        CustomerResponse dto = new CustomerResponse();
        dto.setId(customer.getId());
        dto.setFullName(customer.getFullName());
        dto.setEmail(customer.getEmail());
        return dto;
    }
}
