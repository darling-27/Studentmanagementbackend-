package com.demo.demo.controller;

import com.demo.demo.dto.request.LoginRequest;
import com.demo.demo.dto.response.ApiResponse;
import com.demo.demo.dto.response.AuthResponse;
import com.demo.demo.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Login and token management")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Login with username and password",
            description = "Authenticates a user and returns a JWT bearer token in the response body. Use this token in the Authorization header as Bearer <token>."
    )
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.ok("Login successful", response));
    }

    @Operation(
            summary = "Health check for auth service",
            description = "Public endpoint to verify the auth module is reachable."
    )
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.ok("Auth service is up", "OK"));
    }
}
