package com.kudryavtsevgennady.springrestaurantvoting.spring.util.exception;

public class DeadlineVoteException extends RuntimeException {
    public DeadlineVoteException(String message) {
        super(message);
    }
}
