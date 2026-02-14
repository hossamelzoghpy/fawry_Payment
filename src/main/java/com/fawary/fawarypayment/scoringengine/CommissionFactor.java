package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.RecommendRequestDTO;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CommissionFactor implements ScoringFactor {
    @Override
    public BigDecimal score(ScoredGatewayDto gateway) {
        return gateway.getCommission();
    }

    @Override
    public String getCode() {
        return "commission";
    }

    @Override
    public FactorDirection direction() {
        return FactorDirection.LOWER_BETTER;
    }
}
