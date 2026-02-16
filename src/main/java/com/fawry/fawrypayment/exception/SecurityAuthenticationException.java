package com.fawry.fawrypayment.exception;

import org.springframework.http.HttpStatus;

public class SecurityAuthenticationException extends ApplicationException {
    public SecurityAuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
