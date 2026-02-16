package com.fawary.fawarypayment.service.scoringengine;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FactorRegistry {
    private final Map<String, ScoringFactor> codeToFactorMapping;


    public FactorRegistry(List< ScoringFactor> availableFactors) {
        codeToFactorMapping =  availableFactors.stream().map(
                factor -> Map.entry(factor.getCode(), factor)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    public Optional<ScoringFactor> getFactor(String code) {
        ScoringFactor factor = codeToFactorMapping.get(code);
        return Optional.ofNullable(factor);
    }
}
