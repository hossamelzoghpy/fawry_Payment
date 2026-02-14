package com.fawary.fawarypayment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transaction_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog {
    @Id
    private UUID id;

    @Column(name = "biller_id")
    private String billerId;

    @Column(name = "gateway_id")
    private String gatewayId;

    private BigDecimal amount;

    private BigDecimal commission;

    private String urgency;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
