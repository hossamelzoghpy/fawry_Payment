package com.fawary.fawarypayment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayConfigDTO {
    private String id;

    @NotEmpty
    private String name;

    private BigDecimal fixedFee;

    @Max(100)
    @Min(0)
    private BigDecimal percentageFee;

    @Positive
    private BigDecimal dailyLimit;

    @Positive
    private BigDecimal minTransaction;

    @Positive
    private BigDecimal maxTransaction;

    @PositiveOrZero
    private Integer processingTime;
    private Boolean enabled;
}
