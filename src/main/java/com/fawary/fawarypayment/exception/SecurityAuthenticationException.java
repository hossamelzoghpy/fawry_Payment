package com.fawary.fawarypayment.exception;

import org.springframework.security.core.AuthenticationException;

public class SecurityAuthenticationException extends AuthenticationException {
    public SecurityAuthenticationException(String message) {
        super(message);
    }
}
