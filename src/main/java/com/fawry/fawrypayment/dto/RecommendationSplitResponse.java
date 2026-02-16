package com.fawry.fawrypayment.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class RecommendationSplitResponse {
    private String selectedGateway;
    private boolean requiresSplitting;
    private List<BigDecimal> splits;
    private BigDecimal totalCommission;
    private boolean quotaAvailable;
    private int splitCount;
}
