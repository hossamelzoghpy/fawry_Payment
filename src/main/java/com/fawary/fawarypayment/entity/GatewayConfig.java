package com.fawary.fawarypayment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "gateway_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayConfig {
    @Id
    private String id;


    private String name;

    @Column(name = "fixed_fee")
    private BigDecimal fixedFee;

    @Column(name = "percentage_fee")
    private BigDecimal percentageFee;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "min_transaction")
    private BigDecimal minTransaction;

    @Column(name = "max_transaction")
    private BigDecimal maxTransaction;

    @Column(name = "processing_time")
    private Integer processingTime;

    private Boolean enabled;

}
