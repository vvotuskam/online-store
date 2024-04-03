package com.halyk.onlinestore.security;

import com.halyk.onlinestore.model.enums.JwtTokenType;
import com.halyk.onlinestore.repository.JwtTokenRepository;
import com.halyk.onlinestore.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userService;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);
        var optionalUsername = jwtUtils.extractUsername(jwt);

        boolean noAuthentication = SecurityContextHolder.getContext().getAuthentication() == null;

        if (optionalUsername.isPresent() && noAuthentication) {
            String username = optionalUsername.get();
            UserDetails userDetails = userService.loadUserByUsername(username);

            boolean isValid = jwtTokenRepository
                                      .findByToken(jwt)
                                      .filter(j -> j.getType() == JwtTokenType.ACCESS)
                                      .isPresent()
                              && jwtUtils.validate(jwt, userDetails);
            if (isValid) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
