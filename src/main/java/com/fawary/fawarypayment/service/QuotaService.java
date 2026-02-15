package com.fawary.fawarypayment.service;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.repo.BillerRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class QuotaService {

    private final BillerRepo usageRepository;
    private final TransactionLogService transactionLogService;



    public QuotaService(BillerRepo usageRepository, TransactionLogService transactionLogService) {
        this.usageRepository = usageRepository;
        this.transactionLogService = transactionLogService;
    }

    public BigDecimal getUsedAmount(String billerId, String gatewayId, LocalDate date) {
        validateIds(billerId, gatewayId);
        if (date == null) throw new IllegalArgumentException("date must not be null");
        return transactionLogService.getTotalTransactionsAmountByBillerInDay(billerId, gatewayId, date);
    }


    public BigDecimal getRemainingQuota(String billerId, GatewayConfigDTO gateway, LocalDate date) {
        if (gateway == null) throw new IllegalArgumentException("gateway must not be null");
        validateIds(billerId, gateway.getId());
        if (date == null) throw new IllegalArgumentException("date must not be null");

        BigDecimal used = getUsedAmount(billerId, gateway.getId(), date);
        BigDecimal dailyLimit = nullProtect(gateway.getDailyLimit());

        BigDecimal remaining = dailyLimit.subtract(used);

        return remaining.max(BigDecimal.ZERO);
    }

    public boolean canConsume(String billerId, GatewayConfigDTO gateway, BigDecimal amount, LocalDate date) {
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        if (amount.signum() < 0) throw new IllegalArgumentException("amount must be >= 0");

        BigDecimal remaining = getRemainingQuota(billerId, gateway, date);
        return remaining.compareTo(amount) >= 0;
    }



    private void validateIds(String billerId, String gatewayId) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new IllegalArgumentException("gatewayId must not be blank");
        }
    }

    private BigDecimal nullProtect(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}

