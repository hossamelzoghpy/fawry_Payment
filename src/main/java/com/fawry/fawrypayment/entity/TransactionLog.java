package com.fawry.fawrypayment.entity;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "gateway_id")
    private GatewayConfig gateway;

    private BigDecimal amount;

    private BigDecimal commission;

    private String urgency;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
