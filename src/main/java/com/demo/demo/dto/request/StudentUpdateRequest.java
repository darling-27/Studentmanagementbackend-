package com.demo.demo.dto.request;

import jakarta.validation.constraints.*;

public record StudentUpdateRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Size(max = 50)  String rollNo,
        @Size(max = 100)           String course,
        @Pattern(regexp = "^$|^[0-9+\\-\\s]{7,20}$", message = "invalid phone")
        String phone,
        @Email @Size(max = 150)    String email
) {}
