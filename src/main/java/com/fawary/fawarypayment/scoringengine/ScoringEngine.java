package com.fawary.fawarypayment.scoringengine;

import com.fawary.fawarypayment.dto.FactorDirection;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.dto.ScoredGatewayDto;
import com.fawary.fawarypayment.dto.ScoringFactorConfigDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ScoringEngine {
    private final FactorRegistry factorRegistry;
    private final ScoringFactorConfigService scoringFactorConfigService;

    private static final Pair<BigDecimal, BigDecimal> range = Pair.of( BigDecimal.valueOf(.1), BigDecimal.valueOf(.9));


    public ScoringEngine(FactorRegistry factorRegistry, ScoringFactorConfigService scoringFactorConfigService) {
        this.factorRegistry = factorRegistry;
        this.scoringFactorConfigService = scoringFactorConfigService;
    }
    public List<ScoredGatewayDto> rankGateways(List<ScoredGatewayDto> gateways){
        List<ScoringFactorConfigDto> activeFactorConfigs = scoringFactorConfigService.getActiveFactorConfigs();
        Map<String, Pair<BigDecimal, BigDecimal>> factorsMinMax = new HashMap<>();

        for (ScoringFactorConfigDto activeFactorConfig:activeFactorConfigs){
            ScoringFactor scoringFactor = factorRegistry.getFactor(activeFactorConfig.code());
            BigDecimal initialMin = scoringFactor.score(gateways.get(0));
            BigDecimal initialMax = scoringFactor.score(gateways.get(0));
            factorsMinMax.put(activeFactorConfig.code(), Pair.of(initialMin, initialMax));
            for (ScoredGatewayDto gateway:gateways){
                BigDecimal factorScore = scoringFactor.score(gateway);
                BigDecimal min = factorsMinMax.get(activeFactorConfig.code()).getFirst().min(factorScore);
                BigDecimal max = factorsMinMax.get(activeFactorConfig.code()).getSecond().max(factorScore);
                factorsMinMax.put(activeFactorConfig.code(), Pair.of(min, max));
            }

        }
        for (ScoredGatewayDto gateway:gateways){
            BigDecimal normalizedScore = BigDecimal.ZERO.setScale(4);
            BigDecimal weightsum = BigDecimal.ZERO.setScale(4);
            log.info("Scoring gateway: {}", gateway.getGateway().getName());
            for (ScoringFactorConfigDto activeFactorConfig:activeFactorConfigs){
                ScoringFactor scoringFactor = factorRegistry.getFactor(activeFactorConfig.code());
                FactorDirection direction = scoringFactor.direction();
                BigDecimal nonNormalizedfactorScore = scoringFactor.score(gateway);
                BigDecimal normalizedFactorScore = normalizeShifted(nonNormalizedfactorScore, factorsMinMax.get(activeFactorConfig.code()), direction);
                BigDecimal factorScore = activeFactorConfig.weight().multiply(normalizedFactorScore);
                normalizedScore = normalizedScore.add(factorScore);
                weightsum = weightsum.add(activeFactorConfig.weight());
                log.info("GW: {} Factor: {} Score: {}",gateway.getGateway().getName(), activeFactorConfig.code(), factorScore);
            }
            log.info("GW: {} Normalized score: {}", gateway.getGateway().getName(), normalizedScore);
            gateway.setScore(normalizedScore.divide(weightsum,  6, RoundingMode.HALF_UP));
        }

        return gateways.stream().sorted(
                Comparator
                        .comparing(ScoredGatewayDto::getScore).reversed()
                ).toList();
    }

    private static BigDecimal normalizeShifted(
            BigDecimal value,
            Pair<BigDecimal, BigDecimal> factorMinMax,
            FactorDirection direction
    ) {

        if (factorMinMax.getFirst().equals(factorMinMax.getSecond())) {
            return (range.getFirst().add(range.getSecond())).divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP) ;
        }

        BigDecimal maxMinusMin = factorMinMax.getSecond().subtract(factorMinMax.getFirst());
        // raw min-max 0..1
        BigDecimal raw = (direction == FactorDirection.LOWER_BETTER)
                ? (factorMinMax.getSecond().subtract(value)).divide(maxMinusMin, 6, RoundingMode.HALF_UP)
                : (value.subtract(factorMinMax.getFirst())).divide(maxMinusMin,  6, RoundingMode.HALF_UP);

        return raw.multiply(range.getSecond().subtract(range.getFirst())).add(range.getFirst());
    }
}
