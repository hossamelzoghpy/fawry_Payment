package com.fawry.fawrypayment.exception;

import org.springframework.http.HttpStatus;

public class NotFountException extends ApplicationException {
    public NotFountException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
