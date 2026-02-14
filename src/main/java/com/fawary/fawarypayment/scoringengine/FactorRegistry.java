package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FactorRegistry {
    private final Map<String, ScoringFactor> codeToFactorMapping;


    public FactorRegistry(List< ScoringFactor> availableFactors) {
        codeToFactorMapping =  availableFactors.stream().map(
                factor -> Map.entry(factor.getCode(), factor)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public ScoringFactor getFactor(String code) {
        ScoringFactor factor = codeToFactorMapping.get(code);
        if (factor== null) throw new IllegalStateException("No factor implementation registered for code: " + code);
        return factor;
    }
}
