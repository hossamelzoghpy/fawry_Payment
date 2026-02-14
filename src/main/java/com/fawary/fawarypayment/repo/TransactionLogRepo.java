package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionLogRepo extends JpaRepository<TransactionLog, UUID> {

    List<TransactionLog> findByBillerIdAndCreatedAtBetween(
            String billerId,
            LocalDateTime start,
            LocalDateTime end
    );

    List<TransactionLog> findByBillerIdAndGatewayIdAndCreatedAtBetween(
            String billerId,
            String gatewayId,
            LocalDateTime start,
            LocalDateTime end
    );
}