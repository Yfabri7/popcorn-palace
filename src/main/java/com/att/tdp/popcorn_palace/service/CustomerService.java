package com.att.tdp.popcorn_palace.service;

import com.att.tdp.popcorn_palace.dto.TicketBookingResponse;
import com.att.tdp.popcorn_palace.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void deleteCustomer(Long id);
    Customer getCustomerById(Long id);
    List<TicketBookingResponse> getBookingsByCustomerId(Long customerId);
}
