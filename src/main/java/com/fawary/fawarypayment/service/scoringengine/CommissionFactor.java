package com.fawary.fawarypayment.service.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
