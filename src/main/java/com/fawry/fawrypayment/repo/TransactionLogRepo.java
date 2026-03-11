package com.fawry.fawrypayment.repo;

import com.fawry.fawrypayment.entity.TransactionLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface TransactionLogRepo extends PagingAndSortingRepository<TransactionLog, UUID>, JpaRepository<TransactionLog, UUID> {

    Page<TransactionLog> findByBillerIdAndCreatedAtBetween(
            String billerId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );

    Page<TransactionLog> findByBillerIdAndGatewayIdAndCreatedAtBetween(
            String billerId,
            String gatewayId,
            LocalDateTime start,
            LocalDateTime end,
            Pageable pageable
    );
    Page<TransactionLog> findAllByBillerId(String billerId, Pageable pageable);
    Page<TransactionLog> findAllByBillerIdAndGatewayId(String billerId, String gatewayId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(t.amount), 0) " +
            "FROM TransactionLog t " +
            "WHERE t.billerId = :billerId " +
            "AND t.gateway.id = :gatewayId " +
            "AND FUNCTION('DATE', t.createdAt) = :date")
    BigDecimal sumAmountByBillerAndGatewayAndDate(
            @Param("billerId") String billerId,
            @Param("gatewayId") String gatewayId,
            @Param("date") LocalDate date
    );


}