package com.fawry.fawrypayment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class ScoringFactorConfigDto{

    @NotEmpty
    String code;

    @Max(1)
    @Min(0)
    BigDecimal weight;
}
