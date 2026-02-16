package com.fawry.fawrypayment.dto;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class RecommendRequestDTO {

    @NotEmpty(message = "Biller Id cannot be empty")
    private String billerId;

    @Positive
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    @NotEmpty(message = "Urgency cannot be empty")
    private String urgency;
}
