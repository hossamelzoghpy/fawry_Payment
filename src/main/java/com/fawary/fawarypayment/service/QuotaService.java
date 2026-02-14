package com.fawary.fawarypayment.service;
import com.fawary.fawarypayment.dto.GatewayConfigDTO;
import com.fawary.fawarypayment.entity.BillerUsage;
import com.fawary.fawarypayment.entity.GatewayConfig;
import com.fawary.fawarypayment.repo.BillerUsageRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class QuotaService {

    private final BillerUsageRepo usageRepository;

    public QuotaService(BillerUsageRepo usageRepository) {
        this.usageRepository = usageRepository;
    }

    public BigDecimal getUsedAmount(String billerId, String gatewayId, LocalDate date) {
        validateIds(billerId, gatewayId);
        if (date == null) throw new IllegalArgumentException("date must not be null");

        return usageRepository
                .findByBillerIdAndGatewayIdAndUsageDate(billerId, gatewayId, date)
                .map(BillerUsage::getUsedAmount)
                .orElse(BigDecimal.ZERO);
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


    @Transactional
    public void consume(String billerId, GatewayConfigDTO gateway, BigDecimal amount, LocalDate date) {
        if (gateway == null) throw new IllegalArgumentException("gateway must not be null");
        validateIds(billerId, gateway.getId());
        if (date == null) throw new IllegalArgumentException("date must not be null");
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        if (amount.signum() <= 0) throw new IllegalArgumentException("amount must be > 0");

        BillerUsage usage = usageRepository
                .findByBillerIdAndGatewayIdAndUsageDate(billerId, gateway.getId(), date)
                .orElseGet(() -> BillerUsage.builder()
                        .billerId(billerId)
                        .gatewayId(gateway.getId())
                        .usageDate(date)
                        .usedAmount(BigDecimal.ZERO)
                        .build());


        usage.setUsedAmount(usage.getUsedAmount().add(amount));

        usageRepository.save(usage);
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

