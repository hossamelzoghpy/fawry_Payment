package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.RecommendRequestDTO;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;

import java.math.BigDecimal;

public interface ScoringFactor {
    BigDecimal score(ScoredGatewayDto scoredGatewayDto);
    String getCode();
    FactorDirection direction();

}
