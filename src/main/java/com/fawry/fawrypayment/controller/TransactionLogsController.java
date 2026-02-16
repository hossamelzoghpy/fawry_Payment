package com.fawry.fawrypayment.controller;

import com.fawry.fawrypayment.dto.PaginatedResponseDto;
import com.fawry.fawrypayment.dto.TransactionDTO;
import com.fawry.fawrypayment.service.TransactionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/billers/logs")
@RequiredArgsConstructor
public class TransactionLogsController {
    private final TransactionLogService transactionLogService;

    @GetMapping("/{billerId}/transactions")
    public PaginatedResponseDto<TransactionDTO> getTransactionsForDay(
            @PathVariable String billerId,
            @RequestParam(required = false) String gatewayId,
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false)
            @DateTimeFormat LocalDate date
    ) {
        LocalDate effectiveDate = (date == null) ? java.time.LocalDate.now() : date;

        if (gatewayId == null || gatewayId.isBlank()) {
            return transactionLogService.getTransactionsForDay(billerId, effectiveDate, pageNumber);
        }
        return transactionLogService.getTransactionsForDay(billerId, gatewayId, effectiveDate, pageNumber);
    }
}
