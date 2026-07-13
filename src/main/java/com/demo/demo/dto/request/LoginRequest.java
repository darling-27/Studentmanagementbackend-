package com.demo.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "username (name or email) is required")
        String username,
        @NotBlank(message = "password is required")
        String password
) {}
