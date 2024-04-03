package com.halyk.onlinestore.service;

import com.halyk.onlinestore.model.User;
import com.halyk.onlinestore.model.enums.JwtTokenType;

public interface JwtTokenService {
    void saveUserToken(User user, String jwt, JwtTokenType tokenType);
}
