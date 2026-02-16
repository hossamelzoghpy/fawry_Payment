package com.fawry.fawrypayment.entity;

import jakarta.persistence.*;
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
