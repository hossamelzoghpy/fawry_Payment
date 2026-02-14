package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.RecommendRequestDTO;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import com.fawary.fawarypayment.service.QuotaService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

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
