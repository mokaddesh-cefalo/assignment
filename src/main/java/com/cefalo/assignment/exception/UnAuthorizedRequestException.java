package com.cefalo.assignment.exception;

public class UnAuthorizedRequestException extends RuntimeException {
    public UnAuthorizedRequestException(String message) {
        super(message);
    }
}
