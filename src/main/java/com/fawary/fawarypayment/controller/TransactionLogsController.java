package com.fawary.fawarypayment.controller;

import com.fawary.fawarypayment.entity.TransactionLog;
import com.fawary.fawarypayment.service.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/billers/logs")
@RequiredArgsConstructor
public class TransactionLogsController {
    private final TransactionLogService transactionLogService;

    @GetMapping("/{billerId}/transactions")
    public List<TransactionLog> getTransactionsForDay(
            @PathVariable String billerId,
            @RequestParam(required = false) String gatewayId,
            @RequestParam(required = false)
            @DateTimeFormat LocalDate date
    ) {
        LocalDate effectiveDate = (date == null) ? LocalDate.now() : date;

        if (gatewayId == null || gatewayId.isBlank()) {
            return transactionLogService.getTransactionsForDay(billerId, effectiveDate);
        }
        return transactionLogService.getTransactionsForDay(billerId, gatewayId, effectiveDate);
    }

}
