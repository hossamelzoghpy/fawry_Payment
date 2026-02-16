package com.fawry.fawrypayment.service.scoringengine;

import com.fawry.fawrypayment.dto.FactorDirection;
import com.fawry.fawrypayment.dto.ScoredGatewayDto;

import java.math.BigDecimal;

public interface ScoringFactor {
    BigDecimal score(ScoredGatewayDto scoredGatewayDto);
    String getCode();
    FactorDirection direction();

}
