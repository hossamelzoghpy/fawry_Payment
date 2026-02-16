package com.fawry.fawrypayment.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException{

    private final HttpStatus code;

//    public ApplicationException(String message, HttpStatus code, Throwable cause) {
//        super(message, cause);
//        this.code = code;
//    }

    public ApplicationException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }
}
