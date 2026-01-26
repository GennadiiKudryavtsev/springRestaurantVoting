package com.kudryavtsevgennady.springrestaurantvoting.spring.util.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
