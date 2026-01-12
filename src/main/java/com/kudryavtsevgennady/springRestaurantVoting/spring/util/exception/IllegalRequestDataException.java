package com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception;

public class IllegalRequestDataException extends RuntimeException {
    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}
