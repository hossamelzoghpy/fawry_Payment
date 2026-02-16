package com.fawry.fawrypayment.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequestDto(@NotEmpty String username,@NotEmpty String password) {
}
