package com.halyk.onlinestore.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halyk.onlinestore.dto.ErrorResponse;
import com.halyk.onlinestore.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, ObjectMapper mapper
    ) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(authenticationProvider)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(matcher -> {
                    matcher.requestMatchers("/api/auth", "/api/auth/refresh").permitAll();
                    matcher.anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> {
                    exceptions.authenticationEntryPoint((request, response, authException) -> {
                        ErrorResponse unauthorized = ErrorResponse.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .message("Unauthorized")
                                .timestamp(LocalDateTime.now())
                                .build();

                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream()
                                .write(mapper.writeValueAsBytes(unauthorized));
                    });
                });

        return http.build();
    }


}
