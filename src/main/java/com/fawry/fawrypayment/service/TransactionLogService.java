package com.fawry.fawrypayment.service;

import com.fawry.fawrypayment.dto.PaginatedResponseDto;
import com.fawry.fawrypayment.dto.TransactionDTO;
import com.fawry.fawrypayment.entity.TransactionLog;
import com.fawry.fawrypayment.mapper.TransactionMapper;
import com.fawry.fawrypayment.repo.TransactionLogRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public PaginatedResponseDto<TransactionDTO> getTransactionsForDay(String billerId, LocalDate date, Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(10).withPage(pageNumber);
        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
//        if (date == null) {
//            throw new IllegalArgumentException("date must not be null");
//        }
        LocalDateTime start=null;
        LocalDateTime end=null;
        if(date!=null){
            start=date.atStartOfDay();
            end=date.plusDays(1).atStartOfDay();
        }
        Page<TransactionLog> dataPage=null;
        if(date!=null){
            dataPage = transactionLogRepository.findByBillerIdAndCreatedAtBetween(billerId, start, end, pageable);

        }
        else{
            dataPage=transactionLogRepository.findAllByBillerId(billerId,pageable);
        }


        PaginatedResponseDto<TransactionDTO> response = new PaginatedResponseDto<>();
        response.setTotalPages(dataPage.getTotalPages());
        response.setPageSize(dataPage.getSize());
        response.setCurrentPage(dataPage.getNumber());
        response.setData(transactionMapper.toDtoList(dataPage.getContent()));
        return response;
    }
    public PaginatedResponseDto<TransactionDTO> getTransactionsForDay(String billerId, String gatewayId, LocalDate date, Integer pageNumber) {
        Pageable pageable = Pageable.ofSize(10).withPage(pageNumber);

        if (billerId == null || billerId.isBlank()) {
            throw new IllegalArgumentException("billerId must not be blank");
        }
        if (gatewayId == null || gatewayId.isBlank()) {
            throw new IllegalArgumentException("gatewayId must not be blank");
        }
        LocalDateTime start=null;
        LocalDateTime end=null;
        if(date!=null){
            start=date.atStartOfDay();
            end=date.plusDays(1).atStartOfDay();
        }
        Page<TransactionLog> dataPage=null;
        if(date!=null){
            dataPage = transactionLogRepository.findByBillerIdAndGatewayIdAndCreatedAtBetween(billerId, gatewayId, start, end, pageable);

        }
        else{
            dataPage=transactionLogRepository.findAllByBillerIdAndGatewayId(billerId,gatewayId,pageable);
        }

        PaginatedResponseDto<TransactionDTO> response = new PaginatedResponseDto<>();
        response.setTotalPages(dataPage.getTotalPages());
        response.setPageSize(dataPage.getSize());
        response.setCurrentPage(dataPage.getNumber());
        response.setData(transactionMapper.toDtoList(dataPage.getContent()));
        return response;
    }


    BigDecimal getTotalTransactionsAmountByBillerInDay(String billerId,
                                                       String gatewayId,
                                                       LocalDate date){

        return transactionLogRepository.sumAmountByBillerAndGatewayAndDate(billerId,gatewayId,date);

    }
}

