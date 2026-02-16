package com.fawry.fawrypayment.service.scoringengine;

import com.fawry.fawrypayment.dto.FactorDirection;
import com.fawry.fawrypayment.dto.ScoredGatewayDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProcessingTimeFactor implements ScoringFactor {
    @Override
    public BigDecimal score(ScoredGatewayDto scoredGatewayDto) {
        return BigDecimal.valueOf(scoredGatewayDto.getProcessingTime());
    }

    @Override
    public String getCode() {
        return "processingTime";
    }

    @Override
    public FactorDirection direction() {
        return FactorDirection.LOWER_BETTER;
    }
}
