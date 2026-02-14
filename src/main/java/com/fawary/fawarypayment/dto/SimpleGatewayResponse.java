package com.fawary.fawarypayment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@lombok.Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleGatewayResponse{
        String id;
        String name;
        BigDecimal estimatedCommission;
        String processingTime;
}
