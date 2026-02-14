package com.fawary.fawarypayment.service;

import com.fawary.fawarypayment.dto.TransactionDTO;
import com.fawary.fawarypayment.entity.TransactionLog;
import com.fawary.fawarypayment.mapper.TransactionMapper;
import com.fawary.fawarypayment.repo.TransactionLogRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionLogService {

    private final TransactionLogRepo transactionLogRepository;
    private final TransactionMapper transactionMapper;

    public TransactionLogService(TransactionLogRepo transactionLogRepository, TransactionMapper transactionMapper) {
        this.transactionLogRepository = transactionLogRepository;
        this.transactionMapper = transactionMapper;
    }

    public TransactionLog logRecommendation(String billerId,
                                            String gatewayId,
                                            BigDecimal amount,
                                            BigDecimal commission,
                                            String urgency) {

//        validateInputs(billerId, gatewayId, amount, commission, urgency);

        TransactionDTO log = TransactionDTO.builder()
                .id(UUID.randomUUID())
                .billerId(billerId)
                .gatewayId(gatewayId)
                .amount(amount)
                .commission(commission)
                .urgency(urgency.toUpperCase())
                .status("RECOMMENDED")
                .createdAt(LocalDateTime.now())
                .build();
        TransactionLog entity=transactionMapper.toEntity(log);
        return transactionLogRepository.save(entity);
    }
    public List<TransactionLog> getTransactionsForDay(String billerId, LocalDate date) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return transactionLogRepository.findByBillerIdAndCreatedAtBetween(billerId, start, end);
    }
    public List<TransactionLog> getTransactionsForDay(String billerId, String gatewayId, LocalDate date) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new IllegalArgumentException("gatewayId must not be blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return transactionLogRepository.findByBillerIdAndGatewayIdAndCreatedAtBetween(
                billerId, gatewayId, start, end
        );
    }

    private void validateInputs(String billerId,
                                String gatewayId,
                                BigDecimal amount,
                                BigDecimal commission,
                                String urgency) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new IllegalArgumentException("gatewayId must not be blank");
        }
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be > 0");
        }
        if (commission == null || commission.signum() < 0) {
            throw new IllegalArgumentException("commission must be >= 0");
        }
        if (urgency == null || urgency.isBlank()) {
            throw new IllegalArgumentException("urgency must not be blank");
        }
    }
}

