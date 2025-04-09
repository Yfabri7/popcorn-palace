// File: src/main/java/com/att/tdp/popcorn_palace/exception/DuplicateMovieException.java
package com.att.tdp.popcorn_palace.exception;

public class DuplicateMovieException extends RuntimeException {
    public DuplicateMovieException(String message) {
        super(message);
    }
}
