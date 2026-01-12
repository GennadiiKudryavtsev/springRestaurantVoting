package com.kudryavtsevgennady.springRestaurantVoting.spring.util.exception;

public class DeadlineVoteException extends RuntimeException {
    public DeadlineVoteException(String message) {
        super(message);
    }
}
