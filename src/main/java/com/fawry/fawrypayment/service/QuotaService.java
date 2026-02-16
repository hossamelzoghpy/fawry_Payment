package com.fawry.fawrypayment.service;
import com.fawry.fawrypayment.dto.GatewayConfigDTO;
import com.fawry.fawrypayment.exception.ApplicationException;
import com.fawry.fawrypayment.repo.BillerRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class QuotaService {

    private final TransactionLogService transactionLogService;
    private final BillerRepo billerRepo;
    private final GatewayConfigService gatewayConfigService;



    public QuotaService(TransactionLogService transactionLogService, BillerRepo billerRepo, GatewayConfigService gatewayConfigService) {
        this.transactionLogService = transactionLogService;
        this.billerRepo = billerRepo;
        this.gatewayConfigService = gatewayConfigService;
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
            throw new ApplicationException("billerId must not be blank", HttpStatus.BAD_REQUEST);
        }
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new ApplicationException("gatewayId must not be blank", HttpStatus.BAD_REQUEST);
        }
        if (billerRepo.findById(billerId).isEmpty()) {
            throw new ApplicationException("biller with id: " + billerId + " not found", HttpStatus.BAD_REQUEST);
        }
        if (gatewayConfigService.getGateway(gatewayId) == null) {
            throw new ApplicationException("gateway with id: " + gatewayId + " not found", HttpStatus.BAD_REQUEST);
        }

    }

    private BigDecimal nullProtect(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}

