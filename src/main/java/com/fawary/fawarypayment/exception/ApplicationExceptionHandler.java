package com.fawary.fawarypayment.exception;

import com.fawary.fawarypayment.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex){
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .reduce((msg1, msg2) -> msg1 + ", " + msg2)
                .orElse("Validation failed");

        ErrorResponse response = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(errorMessage)
                .time(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(response);
    }




    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationExcepion(ApplicationException ex){
        log.error("Generic application exception: ", ex);
        ErrorResponse error=ErrorResponse.builder().message(ex.getMessage()).status(ex.getCode().toString()).time(LocalDateTime.now()).build();
        return ResponseEntity
                .status(ex.getCode())
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

