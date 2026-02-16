package com.fawry.fawrypayment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillerUsageDTO {
    private String billerId;
    private String gatewayId;
    private LocalDate usageDate;
    private BigDecimal usedAmount;
}
