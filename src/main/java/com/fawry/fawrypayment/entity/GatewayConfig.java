package com.fawry.fawrypayment.entity;

import com.fawry.fawrypayment.entity.sequence.GatewayId;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "gateway_config" ,uniqueConstraints = @UniqueConstraint(name="uk_gateway_name", columnNames = {"name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GatewayConfig {

    @Id
    @GatewayId
    private String id;

    @Column(name = "name")
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

    @OneToMany(
            mappedBy = "gateway")
    private List<GatewayAvailability> availabilities;

    @Column(name = "processing_time")
    private Integer processingTime;

    @OneToMany(mappedBy = "gateway")
    private List<TransactionLog> transactionLogs;

    private Boolean enabled;

}
