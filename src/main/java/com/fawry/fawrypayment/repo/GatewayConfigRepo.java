package com.fawry.fawrypayment.repo;

import com.fawry.fawrypayment.entity.GatewayConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GatewayConfigRepo extends JpaRepository<GatewayConfig, String> {

    List<GatewayConfig> findByEnabledTrue();
    @Query("SELECT gw.maxTransaction FROM GatewayConfig gw WHERE gw.id = :id")
    BigDecimal findMaxTransactionById(@Param("id") String id);
    @Query("SELECT gw.dailyLimit FROM GatewayConfig gw WHERE gw.id = :id")
    BigDecimal findDailyLimitById(@Param("id") String id);

    @Query("SELECT gw.minTransaction FROM GatewayConfig gw WHERE gw.id = :id")
    BigDecimal findMinTransactionById(@Param("id") String id);
}

