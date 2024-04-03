package com.halyk.onlinestore.utils;

import com.halyk.onlinestore.config.properties.JwtProperties;
import com.halyk.onlinestore.model.enums.JwtTokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtUtils {

    private final JwtProperties jwtProperties;

    public String generateJwt(UserDetails user, JwtTokenType tokenType) {
        long expirationTime = switch (tokenType) {
            case ACCESS -> jwtProperties.getAccessExpiration();
            case REFRESH -> jwtProperties.getRefreshExpiration();
        };

        Date currentDate = new Date();

        Instant instant = currentDate.toInstant().plus(Duration.ofHours(expirationTime));
        Date expirationDate = Date.from(instant);

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities())
                .setIssuedAt(currentDate)
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Optional<String> extractUsername(String jwt) {
        try {
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return Optional.of(claims.getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validate(String jwt, UserDetails userDetails) {
        return extractUsername(jwt)
                .filter(s -> s.equals(userDetails.getUsername()) && !isTokenExpired(jwt))
                .isPresent();
    }

    private boolean isTokenExpired(String jwtToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(this.getSignInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        return claims.getExpiration().before(new Date());
    }
}
