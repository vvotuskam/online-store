package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.model.JwtToken;
import com.halyk.onlinestore.model.User;
import com.halyk.onlinestore.model.enums.JwtTokenType;
import com.halyk.onlinestore.repository.JwtTokenRepository;
import com.halyk.onlinestore.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenRepository jwtTokenRepository;

    @Override
    @Transactional
    public void saveUserToken(User user, String jwt, JwtTokenType tokenType) {
        JwtToken jwtToken = JwtToken.builder()
                .token(jwt)
                .user(user)
                .type(tokenType)
                .build();
        jwtTokenRepository.save(jwtToken);
    }
}
