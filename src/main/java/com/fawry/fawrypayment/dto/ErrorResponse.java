package com.fawry.fawrypayment.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class ErrorResponse {
    private String status;
    private String errorMessage;
    private LocalDateTime time;
}
