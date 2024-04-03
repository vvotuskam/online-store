package com.halyk.onlinestore.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("spring.security.jwt")
public class JwtProperties {
    private String secret;
    private Long accessExpiration;
    private Long refreshExpiration;
}
