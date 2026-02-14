package com.fawary.fawarypayment.repo;

import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoringFactorConfigRepo extends JpaRepository<ScoringFactoringConfig, String> {
    List<ScoringFactoringConfig> findByEnabledTrue();
}
