package com.fawary.fawarypayment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "biller_usage")
@IdClass(BillerUsageId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillerUsage {
    @Id
    @Column(name = "biller_id")
    private String billerId;

    @Id
    @Column(name = "gateway_id")
    private String gatewayId;

    @Id
    @Column(name = "usage_date")
    private LocalDate usageDate;

    @Column(name = "used_amount")
    private BigDecimal usedAmount;

}
