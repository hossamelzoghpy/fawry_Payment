package com.fawary.fawarypayment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(@NotEmpty String username,@NotEmpty String password) {
}
