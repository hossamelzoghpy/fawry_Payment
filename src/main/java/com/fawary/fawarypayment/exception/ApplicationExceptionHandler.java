package com.fawary.fawarypayment.exception;

import com.fawary.fawarypayment.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler {
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex){
            ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).build();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(error);

        }
    @ExceptionHandler(NotFountException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFountException ex){
        ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).
                status(HttpStatus.NOT_FOUND.toString()).time(LocalDateTime.now()).build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);

    }
    @ExceptionHandler(SecurityAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleSecurityAuth(SecurityAuthenticationException ex){
        ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).status(HttpStatus.UNAUTHORIZED.toString()).time(LocalDateTime.now()).build();
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(DataIntegrityViolationException ex) {
            String message = ex.getMessage();
        if (ex.getCause() instanceof ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();
            message = "Database constraint violation: " + constraintName;
        }

        ErrorResponse error=ErrorResponse.builder().message(message).status(HttpStatus.BAD_REQUEST.toString()).time(LocalDateTime.now()).build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnCategorizedExceptions(Exception ex){
            log.error("Uncategorized exception: ", ex);
            String message = "An error occurred while processing your request.";
        ErrorResponse error=ErrorResponse.builder().message(message).status(HttpStatus.INTERNAL_SERVER_ERROR.toString()).time(LocalDateTime.now()).build();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

}

