package com.fawary.fawarypayment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayConfigDTO {
    private String id;
    private String name;
    private BigDecimal fixedFee;
    private BigDecimal percentageFee;
    private BigDecimal dailyLimit;
    private BigDecimal minTransaction;
    private BigDecimal maxTransaction;
    private Integer processingTime;
    private Boolean enabled;
}
