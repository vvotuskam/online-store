package com.halyk.onlinestore.service.impl;

import com.halyk.onlinestore.dto.auth.request.AuthRequest;
import com.halyk.onlinestore.dto.auth.request.RefreshRequest;
import com.halyk.onlinestore.dto.auth.response.AuthResponse;
import com.halyk.onlinestore.exception.AuthorizationException;
import com.halyk.onlinestore.model.JwtToken;
import com.halyk.onlinestore.model.User;
import com.halyk.onlinestore.model.enums.JwtTokenType;
import com.halyk.onlinestore.repository.JwtTokenRepository;
import com.halyk.onlinestore.repository.UserRepository;
import com.halyk.onlinestore.service.AuthService;
import com.halyk.onlinestore.service.JwtTokenService;
import com.halyk.onlinestore.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        tryAuthorize(request.getUsername(), request.getPassword());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(AuthorizationException::new);

        String accessToken = jwtUtils.generateJwt(user, JwtTokenType.ACCESS);
        jwtTokenService.saveUserToken(user, accessToken, JwtTokenType.ACCESS);

        String refreshToken = jwtUtils.generateJwt(user, JwtTokenType.REFRESH);
        jwtTokenService.saveUserToken(user, refreshToken, JwtTokenType.REFRESH);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(RefreshRequest request) {
        return null;
    }

    private void tryAuthorize(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            username, password
                    )
            );
        } catch (BadCredentialsException e) {
            throw new AuthorizationException();
        }
    }
}
