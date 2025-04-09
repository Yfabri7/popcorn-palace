// File: src/main/java/com/att/tdp/popcorn_palace/dto/CustomerResponse.java
package com.att.tdp.popcorn_palace.dto;

public class CustomerResponse {

    private Long id;
    private String fullName;
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
