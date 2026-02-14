package com.fawary.fawarypayment.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDTO {
    private UUID id;
    private String billerId;
    private String gatewayId;
    private BigDecimal amount;
    private BigDecimal commission;
    private String urgency;
    private String status;
    private LocalDateTime createdAt;
}

