package com.fawary.fawarypayment.service.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class QuotaRemainingFactor implements ScoringFactor {


    @Override
    public BigDecimal score(ScoredGatewayDto scoredGatewayDto) {
        return  scoredGatewayDto.getRemainingQuota();
    }

    @Override
    public String getCode() {
        return "remainingQuota";
    }

    @Override
    public FactorDirection direction() {
        return FactorDirection.HIGHER_BETTER;
    }
}
