package com.example.auth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {
    private String issuer;
    private long accessTokenMinutes;
    private long refreshTokenDays;
    private String privateKey;
    private String publicKey;

}
