package com.fawry.fawrypayment.repo;

import com.fawry.fawrypayment.entity.GatewayConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GatewayConfigRepo extends JpaRepository<GatewayConfig, String> {

    List<GatewayConfig> findByEnabledTrue();
}

