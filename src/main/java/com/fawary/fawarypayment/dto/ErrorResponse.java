package com.fawary.fawarypayment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorResponse {
    private String status;
    private String message;
    private LocalDateTime time;
}
