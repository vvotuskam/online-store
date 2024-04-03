package com.halyk.onlinestore.service;

import com.halyk.onlinestore.dto.auth.request.AuthRequest;
import com.halyk.onlinestore.dto.auth.request.RefreshRequest;
import com.halyk.onlinestore.dto.auth.response.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest request);

    AuthResponse refresh(RefreshRequest request);
}
