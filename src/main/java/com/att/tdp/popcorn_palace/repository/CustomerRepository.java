package com.att.tdp.popcorn_palace.repository;

import com.att.tdp.popcorn_palace.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
