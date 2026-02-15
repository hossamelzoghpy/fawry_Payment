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

    public TransactionDTO logTransaction(String billerId,
                                         String gatewayId,
                                         BigDecimal amount,
                                         BigDecimal commission,
                                         String urgency) {

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
        return transactionMapper.toDto(transactionLogRepository.save(entity)) ;
    }
    public List<TransactionDTO> getTransactionsForDay(String billerId, LocalDate date) {
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (date == null) {
            throw new IllegalArgumentException("date must not be null");
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        return transactionMapper.toDtoList(transactionLogRepository.findByBillerIdAndCreatedAtBetween(billerId, start, end));
    }
    public List<TransactionDTO> getTransactionsForDay(String billerId, String gatewayId, LocalDate date) {
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

        return transactionMapper.toDtoList(transactionLogRepository.findByBillerIdAndGatewayIdAndCreatedAtBetween(
                billerId, gatewayId, start, end
        ));
    }


    BigDecimal getTotalTransactionsAmountByBillerInDay(String billerId,
                                                       String gatewayId,
                                                       LocalDate date){

        return transactionLogRepository.sumAmountByBillerAndGatewayAndDate(billerId,gatewayId,date);

    }
}

