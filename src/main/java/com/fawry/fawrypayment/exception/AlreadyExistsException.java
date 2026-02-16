package com.fawry.fawrypayment.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends ApplicationException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.ALREADY_REPORTED);
    }
}
