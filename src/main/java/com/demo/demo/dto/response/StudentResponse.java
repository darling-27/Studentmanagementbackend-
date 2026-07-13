package com.demo.demo.dto.response;

import java.time.LocalDateTime;

public record StudentResponse(
        Long id,
        String fullName,
        String rollNo,
        String course,
        String phone,
        String imageUrl,
        String username,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String createdBy,
        String updatedBy
) {}
