package com.demo.demo.service;

import com.demo.demo.dto.request.LoginRequest;
import com.demo.demo.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse login(LoginRequest request);
}
