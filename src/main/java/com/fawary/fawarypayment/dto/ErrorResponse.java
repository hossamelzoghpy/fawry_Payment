package com.fawary.fawarypayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorResponse {
    private Boolean status;
    private String message;
    private LocalDateTime time;
}
