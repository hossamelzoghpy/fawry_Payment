package com.fawry.fawrypayment.service;


import com.fawry.fawrypayment.dto.*;
import com.fawry.fawrypayment.exception.ApplicationException;
import com.fawry.fawrypayment.exception.NotFountException;
import com.fawry.fawrypayment.mapper.GatewayConfigMapper;
import com.fawry.fawrypayment.mapper.RecommendationMapper;
import com.fawry.fawrypayment.repo.GatewayConfigRepo;
import com.fawry.fawrypayment.service.scoringengine.ScoringEngine;
import jakarta.transaction.InvalidTransactionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class RoutingService {

    private final GatewayConfigRepo gatewayConfigRepository;
    private final CommissionService commissionService;
    private final AvailabilityService availabilityService;
    private final QuotaService quotaService;
    private final GatewayConfigMapper gatewayConfigMapper;
    private final RecommendationMapper recommendationMapper;
    private final TransactionLogService transactionLogService;
    private final ScoringEngine scoringEngine;
    private final GatewayConfigRepo gatewayConfigRepo;


    public RoutingService(GatewayConfigRepo gatewayConfigRepository,
                          CommissionService commissionService,
                          AvailabilityService availabilityService,
                          QuotaService quotaService, GatewayConfigMapper gatewayConfigMapper, RecommendationMapper recommendationMapper, TransactionLogService transactionLogService, ScoringEngine scoringEngine, GatewayConfigRepo gatewayConfigRepo) {
        this.gatewayConfigRepository = gatewayConfigRepository;
        this.commissionService = commissionService;
        this.availabilityService = availabilityService;
        this.quotaService = quotaService;
        this.gatewayConfigMapper = gatewayConfigMapper;
        this.recommendationMapper = recommendationMapper;
        this.transactionLogService = transactionLogService;
        this.scoringEngine = scoringEngine;
        this.gatewayConfigRepo = gatewayConfigRepo;
    }

    public RecommendationResponse recommend(RecommendRequestDTO recommendRequestDTO) {
        LocalDate today = LocalDateTime.now().toLocalDate();



        List<GatewayConfigDTO> enabled = gatewayConfigMapper.toDtoList(gatewayConfigRepository.findByEnabledTrue());

        List<GatewayConfigDTO> viableConfigs = filterViableGatewayConfigs(recommendRequestDTO, enabled, today);

        List<ScoredGatewayDto> viableGateways = prepareScorintDtosData(recommendRequestDTO, viableConfigs, today);

        if (viableGateways.isEmpty()) {
            throw new NotFountException("No viable gateway found for this request");
        }

        List<ScoredGatewayDto> ranked = scoringEngine.rankGateways(viableGateways);

        ScoredGatewayDto recommended = ranked.get(0);
        transactionLogService.logTransaction(recommendRequestDTO.getBillerId(),
                recommended.getGateway().getId(), recommendRequestDTO.getAmount(),recommended.getCommission(),recommendRequestDTO.getUrgency());
        List<ScoredGatewayDto> alternatives = ranked.subList(1, ranked.size());

        return new RecommendationResponse(
                recommendationMapper.toRecommendedGateway(recommended),
                recommendationMapper.toAlternativeGatewayList(alternatives)
        );
    }

    private List<GatewayConfigDTO> filterViableGatewayConfigs(RecommendRequestDTO recommendRequestDTO, List<GatewayConfigDTO> enabled, LocalDate today) {
        List<GatewayConfigDTO> eligible = new ArrayList<>();

        for (GatewayConfigDTO gw : enabled) {
            log.debug("Evaluating gateway: {} (ID: {})", gw.getName(), gw.getId());

            if (!fitsAmountLimit(gw, recommendRequestDTO.getAmount())) {
                log.info("Gateway {} ruled out: Amount {} is outside limits [min: {}, max: {}]",
                        gw.getName(),
                        recommendRequestDTO.getAmount(),
                        gw.getMinTransaction(),
                        gw.getMaxTransaction());
                continue;
            }
            log.debug("Gateway {} passed amount limit check", gw.getName());

            if (!availabilityService.isAvailableNow(gw.getId())) {
                log.info("Gateway {} ruled out: Not available at current time", gw.getName());
                continue;
            }
            log.debug("Gateway {} passed availability check", gw.getName());

            if (!quotaService.canConsume(recommendRequestDTO.getBillerId(), gw, recommendRequestDTO.getAmount(), today)) {
                log.info("Gateway {} ruled out: Daily quota exceeded for biller {}",
                        gw.getName(),
                        recommendRequestDTO.getBillerId());
                continue;
            }
            log.debug("Gateway {} passed quota check", gw.getName());

//            if (!matchesUrgency(gw, ProcessingTime.valueOf(recommendRequestDTO.getUrgency()))) {
//                log.info("Gateway {} ruled out: Processing time {} does not match urgency {}",
//                        gw.getName(),
//                        gw.getProcessingTime(),
//                        recommendRequestDTO.getUrgency());
//                continue;
//            }
            log.debug("Gateway {} passed urgency check", gw.getName());

            log.info("Gateway {} is eligible for recommendation", gw.getName());
            eligible.add(gw);
        }

        log.info("Total eligible gateways: {} out of {} enabled gateways",
                eligible.size(),
                enabled.size());

        return eligible;
    }
    private List<GatewayConfigDTO> filterViableGatewayConfigsWithoutMaxTxn(RecommendRequestDTO recommendRequestDTO, List<GatewayConfigDTO> enabled, LocalDate today) {
        List<GatewayConfigDTO> eligible = new ArrayList<>();

        for (GatewayConfigDTO gw : enabled) {
            log.debug("Evaluating gateway: {} (ID: {})", gw.getName(), gw.getId());

            if (!fitsAmountLimitMinTxn(gw, recommendRequestDTO.getAmount())) {
                log.info("Gateway {} ruled out: Amount {} is outside limits [min: {}, max: {}]",
                        gw.getName(),
                        recommendRequestDTO.getAmount(),
                        gw.getMinTransaction(),
                        gw.getMaxTransaction());
                continue;
            }
            log.debug("Gateway {} passed amount limit check", gw.getName());

            if (!availabilityService.isAvailableNow(gw.getId())) {
                log.info("Gateway {} ruled out: Not available at current time", gw.getName());
                continue;
            }
            log.debug("Gateway {} passed availability check", gw.getName());

//            if (!quotaService.canConsume(recommendRequestDTO.getBillerId(), gw, recommendRequestDTO.getAmount(), today)) {
//                log.info("Gateway {} ruled out: Daily quota exceeded for biller {}",
//                        gw.getName(),
//                        recommendRequestDTO.getBillerId());
//                continue;
//            }
//            log.debug("Gateway {} passed quota check", gw.getName());

//            if (!matchesUrgency(gw, ProcessingTime.valueOf(recommendRequestDTO.getUrgency()))) {
//                log.info("Gateway {} ruled out: Processing time {} does not match urgency {}",
//                        gw.getName(),
//                        gw.getProcessingTime(),
//                        recommendRequestDTO.getUrgency());
//                continue;
//            }
            log.debug("Gateway {} passed urgency check", gw.getName());

            log.info("Gateway {} is eligible for recommendation", gw.getName());
            eligible.add(gw);
        }

        log.info("Total eligible gateways: {} out of {} enabled gateways",
                eligible.size(),
                enabled.size());

        return eligible;
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
    private boolean fitsAmountLimitMinTxn(GatewayConfigDTO gw, BigDecimal amount) {

        return amount.compareTo(gw.getMinTransaction()) >= 0;
//        if (gw.getMaxTransaction()() != null && amount.compareTo(gw.getMaxTransaction()()) > 0) return false;
    }

//    private boolean matchesUrgency(GatewayConfigDTO gw, ProcessingTime urgency) {
//        if (urgency == ProcessingTime.INSTANT ) {
//            return gw.getProcessingTime() != null && gw.getProcessingTime() == 0;
//        }
//        return true;
//    }

    public RecommendationSplitResponse recommendWithSplitting(RecommendRequestDTO recommendRequestDTO) {
        LocalDate today = LocalDateTime.now().toLocalDate();
        List<GatewayConfigDTO> enabled = gatewayConfigMapper.toDtoList(gatewayConfigRepository.findByEnabledTrue());

        List<GatewayConfigDTO> viableConfigs = filterViableGatewayConfigsWithoutMaxTxn(recommendRequestDTO, enabled, today);

        List<ScoredGatewayDto> viableGateways = prepareScorintDtosData(recommendRequestDTO, viableConfigs, today);

        if (viableGateways.isEmpty()) {
            throw new NotFountException("No viable gateway found for this request");
        }

        List<ScoredGatewayDto> ranked = scoringEngine.rankGateways(viableGateways);

        ScoredGatewayDto recommended = ranked.get(0);
        SimpleGatewayResponse recommendedGateway = recommendationMapper.toRecommendedGateway(recommended);
        GatewayConfigDTO gatewayConfigDTO= gatewayConfigMapper.toDto(gatewayConfigRepo.findById(recommendedGateway.getId()).orElseThrow(
                ()-> new NotFountException("No Configuration for this Gateway")));
        BigDecimal maxTxnLimit = gatewayConfigRepository.findMaxTransactionById(recommendedGateway.getId());
//        BigDecimal minTxnLimit = gatewayConfigRepository.findMinTransactionById(recommendedGateway.getId());
        BigDecimal dailyLimit  = gatewayConfigRepository.findDailyLimitById(recommendedGateway.getId());

        BigDecimal totalAmount = recommendRequestDTO.getAmount();


        List<BigDecimal> splits = calculateSplits(totalAmount, maxTxnLimit);


//        boolean hasInvalidChunk = splits.stream()
//                .anyMatch(chunk -> chunk.compareTo(minTxnLimit) < 0);

//        if (hasInvalidChunk) {
//           throw new ApplicationException("This transaction is less than Min transaction ", HttpStatus.BAD_REQUEST);
//        }


        BigDecimal usedQuota = transactionLogService.getTotalTransactionsAmountByBillerInDay(recommendRequestDTO.getBillerId()
                ,recommendedGateway.getId(),today);

        BigDecimal remainingQuota = dailyLimit.subtract(usedQuota);
        boolean quotaAvailable = remainingQuota.compareTo(totalAmount) >= 0;

        if (!quotaAvailable) {
            throw new NotFountException("Daily quota exceeded for biller " + recommendRequestDTO.getBillerId() +
                    " so no gateway can consume this transaction.");
        }

        BigDecimal totalCommission = BigDecimal.ZERO;
        for (BigDecimal chunkOfTrans : splits) {
            BigDecimal chunkCommission = commissionService.calculate(gatewayConfigDTO, chunkOfTrans);
            totalCommission = totalCommission.add(chunkCommission);

            transactionLogService.logTransaction(
                    recommendRequestDTO.getBillerId(),
                    recommendedGateway.getId(),
                    chunkOfTrans,
                    chunkCommission,
                    recommendRequestDTO.getUrgency()
            );
        }

        return RecommendationSplitResponse.builder()
                .selectedGateway(recommendedGateway.getName())
                .requiresSplitting(splits.size() > 1)
                .splits(splits)
                .totalCommission(totalCommission)
                .quotaAvailable(true)
                .splitCount(splits.size())
                .build();
    }


    private List<BigDecimal> calculateSplits(BigDecimal totalAmount, BigDecimal maxTxnLimit) {
        List<BigDecimal> splits = new ArrayList<>();
        BigDecimal remaining = totalAmount;

        while (remaining.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal chunk = remaining.min(maxTxnLimit);
            splits.add(chunk);
            remaining = remaining.subtract(chunk);
        }

        return splits;
    }
}


