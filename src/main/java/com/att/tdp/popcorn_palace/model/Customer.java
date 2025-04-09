// File: src/main/java/com/att/tdp/popcorn_palace/model/Customer.java
package com.att.tdp.popcorn_palace.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<TicketBooking> bookings;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<TicketBooking> getBookings() { return bookings; }
    public void setBookings(List<TicketBooking> bookings) { this.bookings = bookings; }
}
