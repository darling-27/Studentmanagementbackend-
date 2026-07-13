package com.demo.demo.dto.request;

import jakarta.validation.constraints.*;

public record StudentCreateRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Size(max = 50)  String rollNo,
        @Size(max = 100)           String course,
        @Pattern(regexp = "^$|^[0-9+\\-\\s]{7,20}$", message = "invalid phone")
        String phone,

        // login credentials for this student
        @NotBlank @Size(min = 3, max = 100) String username,
        @Email @Size(max = 150)             String email,
        @NotBlank @Size(min = 12, max = 100) @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{12,}$",
                message = "Password must be >= 12 chars with uppercase, lowercase, digit, and special character"
        ) String password
) {}
