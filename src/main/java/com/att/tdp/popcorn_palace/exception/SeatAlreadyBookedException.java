// File: src/main/java/com/att/tdp/popcorn_palace/exception/SeatAlreadyBookedException.java
package com.att.tdp.popcorn_palace.exception;

public class SeatAlreadyBookedException extends RuntimeException {
    public SeatAlreadyBookedException(String message) {
        super(message);
    }
}