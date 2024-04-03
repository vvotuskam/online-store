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
    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenService jwtTokenService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse login(AuthRequest request) {
        tryAuthorize(request.getUsername(), request.getPassword()); // check user credentials

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(AuthorizationException::new);

        String accessToken = jwtUtils.generateJwt(user, JwtTokenType.ACCESS);
        jwtTokenService.saveUserToken(user, accessToken, JwtTokenType.ACCESS);

        String refreshToken = jwtUtils.generateJwt(user, JwtTokenType.REFRESH);
        jwtTokenService.saveUserToken(user, refreshToken, JwtTokenType.REFRESH);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (jwtUtils.isTokenExpired(refreshToken)) {
            throw new AuthorizationException();
        }

        // check if such refresh exists in db
        JwtToken jwtToken = jwtTokenRepository.findByToken(refreshToken)
                .filter(token -> token.getType() == JwtTokenType.REFRESH)
                .orElseThrow(AuthorizationException::new);

        User user = jwtToken.getUser();

        String newAccessToken = jwtUtils.generateJwt(user, JwtTokenType.ACCESS);
        jwtTokenService.saveUserToken(user, newAccessToken, JwtTokenType.ACCESS);

        String newRefreshToken = jwtUtils.generateJwt(user, JwtTokenType.REFRESH);
        jwtTokenService.saveUserToken(user, newRefreshToken, JwtTokenType.REFRESH);

        return new AuthResponse(newAccessToken, newRefreshToken);
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
