package com.fawary.fawarypayment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "scoring_factoring_config")
@Data
public class ScoringFactoringConfig {
    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "enabled")
    private boolean enabled = true;
}
