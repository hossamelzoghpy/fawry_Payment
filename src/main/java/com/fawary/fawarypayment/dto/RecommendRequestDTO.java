package com.fawary.fawarypayment.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RecommendRequestDTO {

    @NotNull(message = "Biller Id cannot be null")
    @NotEmpty(message = "Biller Id cannot be empty")
    private String billerId;
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;
    @NotNull(message = "Urgency cannot be null")
    @NotEmpty(message = "Urgency cannot be empty")
    private String urgency;
}
