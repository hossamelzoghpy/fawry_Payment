package com.fawary.fawarypayment.repo;

import com.fasterxml.jackson.annotation.OptBoolean;
import com.fawary.fawarypayment.entity.ScoringFactoringConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScoringFactorConfigRepo extends JpaRepository<ScoringFactoringConfig, String> {
    List<ScoringFactoringConfig> findByEnabledTrue();

    Optional<ScoringFactoringConfig> findByCode(String code);
}
