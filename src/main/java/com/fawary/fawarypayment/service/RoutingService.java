package com.fawary.fawarypayment.service;


import com.fawary.fawarypayment.dto.*;
import com.fawary.fawarypayment.mapper.GatewayConfigMapper;
import com.fawary.fawarypayment.mapper.RecommendationMapper;
import com.fawary.fawarypayment.repo.GatewayConfigRepo;
import com.fawary.fawarypayment.scoringengine.ScoringEngine;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class RoutingService {

    private final GatewayConfigRepo gatewayConfigRepository;
    private final CommissionService commissionService;
    private final AvailabilityService availabilityService;
    private final QuotaService quotaService;
    private final GatewayConfigMapper gatewayConfigMapper;
    private final RecommendationMapper recommendationMapper;
    private final TransactionLogService transactionLogService;
    private final ScoringEngine scoringEngine;


    public RoutingService(GatewayConfigRepo gatewayConfigRepository,
                          CommissionService commissionService,
                          AvailabilityService availabilityService,
                          QuotaService quotaService, GatewayConfigMapper gatewayConfigMapper, RecommendationMapper recommendationMapper, TransactionLogService transactionLogService, ScoringEngine scoringEngine) {
        this.gatewayConfigRepository = gatewayConfigRepository;
        this.commissionService = commissionService;
        this.availabilityService = availabilityService;
        this.quotaService = quotaService;
        this.gatewayConfigMapper = gatewayConfigMapper;
        this.recommendationMapper = recommendationMapper;
        this.transactionLogService = transactionLogService;
        this.scoringEngine = scoringEngine;
    }

    public RecommendationResponse recommend(RecommendRequestDTO recommendRequestDTO) {
        LocalDate today = LocalDateTime.now().toLocalDate();

        List<GatewayConfigDTO> enabled = gatewayConfigMapper.toDtoList(gatewayConfigRepository.findByEnabledTrue());

        List<GatewayConfigDTO> viableConfigs = filterViableGatewayConfigs(recommendRequestDTO, enabled, today);

        List<ScoredGatewayDto> viableGateways = prepareScorintDtosData(recommendRequestDTO, viableConfigs, today);

        if (viableGateways.isEmpty()) {
            throw new IllegalStateException("No viable gateway found for this request");
        }

        List<ScoredGatewayDto> ranked = scoringEngine.rankGateways(viableGateways);

        ScoredGatewayDto recommended = ranked.get(0);
        quotaService.consume(recommendRequestDTO.getBillerId(), recommended.getGateway(), recommendRequestDTO.getAmount(), today);
        transactionLogService.logRecommendation(recommendRequestDTO.getBillerId(),
                recommended.getGateway().getId(), recommendRequestDTO.getAmount(),recommended.getCommission(),recommendRequestDTO.getUrgency());
        List<ScoredGatewayDto> alternatives = ranked.subList(1, ranked.size());

        return new RecommendationResponse(
                recommendationMapper.toRecommendedGateway(recommended),
                recommendationMapper.toAlternativeGatewayList(alternatives)
        );
    }

    private List<GatewayConfigDTO> filterViableGatewayConfigs(RecommendRequestDTO recommendRequestDTO, List<GatewayConfigDTO> enabled, LocalDate today) {
        return enabled.stream()
                .filter(gw -> fitsAmountLimit(gw, recommendRequestDTO.getAmount()))
                .filter(gw -> availabilityService.isAvailableNow(gw.getId()))
                .filter(gw -> quotaService.canConsume(recommendRequestDTO.getBillerId(), gw, recommendRequestDTO.getAmount(), today))
                .filter(gw -> matchesUrgency(gw, ProcessingTime.valueOf(recommendRequestDTO.getUrgency()))).toList();
    }

    private List<ScoredGatewayDto> prepareScorintDtosData(RecommendRequestDTO recommendRequestDTO, List<GatewayConfigDTO> viable, LocalDate today) {
        return viable.stream()
                .map(gw -> {
                    BigDecimal commission = commissionService.calculate(gw, recommendRequestDTO.getAmount());
                    BigDecimal remaining = quotaService.getRemainingQuota(recommendRequestDTO.getBillerId(), gw, today);
                    int processing = gw.getProcessingTime() == null ? Integer.MAX_VALUE : gw.getProcessingTime();
                    return ScoredGatewayDto.builder().gateway(gw).commission(commission).
                            remainingQuota(remaining).processingTime(processing).build();
                })
                .toList();
    }

    private boolean fitsAmountLimit(GatewayConfigDTO gw, BigDecimal amount) {

        if (amount.compareTo(gw.getMinTransaction()) < 0) return false;
//        if (gw.getMaxTransaction()() != null && amount.compareTo(gw.getMaxTransaction()()) > 0) return false;
        return gw.getMaxTransaction() == null || amount.compareTo(gw.getMaxTransaction()) <= 0;
    }

    private boolean matchesUrgency(GatewayConfigDTO gw, ProcessingTime urgency) {
        if (urgency == ProcessingTime.INSTANT ) {
            return gw.getProcessingTime() != null && gw.getProcessingTime() == 0;
        }
        return true;
    }

    private void validateInputs(String billerId, BigDecimal amount, String urgency, LocalDateTime requestTime) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        if (urgency == null || urgency.isBlank()) {
            throw new IllegalArgumentException("urgency must not be blank");
        }
        if (!"INSTANT".equalsIgnoreCase(urgency) && !"CAN_WAIT".equalsIgnoreCase(urgency)) {
            throw new IllegalArgumentException("urgency must be INSTANT or CAN_WAIT");
        }
        if (requestTime == null) {
            throw new IllegalArgumentException("requestTime must not be null");
        }
    }





}
