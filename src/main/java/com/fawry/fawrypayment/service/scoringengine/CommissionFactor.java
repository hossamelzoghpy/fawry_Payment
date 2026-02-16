package com.fawry.fawrypayment.service.scoringengine;

import com.fawry.fawrypayment.dto.FactorDirection;
import com.fawry.fawrypayment.dto.ScoredGatewayDto;
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
