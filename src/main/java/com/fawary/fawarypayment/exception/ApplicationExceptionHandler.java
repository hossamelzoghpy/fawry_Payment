package com.fawary.fawarypayment.exception;

import com.fawary.fawarypayment.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
        @ExceptionHandler(IllegalArgumentEx.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentEx ex){
            ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error);

        }
    @ExceptionHandler(NotFountEx.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFountEx ex){
        ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);

    }
    @ExceptionHandler(SecurityAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(SecurityAuthenticationException ex){
        ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

}

