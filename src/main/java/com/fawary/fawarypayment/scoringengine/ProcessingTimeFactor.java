package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.RecommendRequestDTO;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
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
