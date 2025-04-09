// File: src/main/java/com/att/tdp/popcorn_palace/bootstrap/CustomerLoader.java
package com.att.tdp.popcorn_palace.bootstrap;

import com.att.tdp.popcorn_palace.model.Customer;
import com.att.tdp.popcorn_palace.repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class CustomerLoader {

    private final CustomerRepository customerRepository;

    public CustomerLoader(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void loadCustomers() {
        if (customerRepository.count() == 0) {
            Customer alice = new Customer();
            alice.setFullName("Alice Johnson");
            alice.setEmail("alice@example.com");

            Customer bob = new Customer();
            bob.setFullName("Bob Smith");
            bob.setEmail("bob@example.com");

            customerRepository.save(alice);
            customerRepository.save(bob);

            System.out.println("Sample customers loaded into the database.");
        }
    }
}

