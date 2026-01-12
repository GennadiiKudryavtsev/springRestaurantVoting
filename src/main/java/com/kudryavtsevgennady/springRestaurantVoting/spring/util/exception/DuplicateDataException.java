package com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception;

public class DuplicateDataException extends RuntimeException {
    public DuplicateDataException(String message) {
        super(message);
    }
}
