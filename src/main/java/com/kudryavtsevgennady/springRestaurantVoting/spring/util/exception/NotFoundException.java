package com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
