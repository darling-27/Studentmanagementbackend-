package com.demo.demo.dto.response;

import java.util.Set;

public record AuthResponse(
        String accessToken,
        String tokenType,
        String username,
        Set<String> roles
) {
    public AuthResponse(String accessToken, String username, Set<String> roles) {
        this(accessToken, "Bearer", username, roles);
    }
}
