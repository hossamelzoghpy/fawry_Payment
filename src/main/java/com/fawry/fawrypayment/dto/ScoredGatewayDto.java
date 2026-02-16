package com.fawry.fawrypayment.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder
public class ScoredGatewayDto{
        GatewayConfigDTO gateway;
        BigDecimal commission;
        BigDecimal remainingQuota;
        Integer processingTime;
        BigDecimal score;
 }

