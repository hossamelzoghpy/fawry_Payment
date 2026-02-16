package com.fawary.fawarypayment.exception;

import org.springframework.http.HttpStatus;

public class SecurityAuthenticationException extends ApplicationException {
    public SecurityAuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
