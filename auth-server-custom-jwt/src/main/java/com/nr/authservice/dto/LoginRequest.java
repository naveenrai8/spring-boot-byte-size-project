package com.nr.authservice.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "email can't be blank")
        String email,
        @NotBlank(message = "password can't be blank")
        String password
) {
}
