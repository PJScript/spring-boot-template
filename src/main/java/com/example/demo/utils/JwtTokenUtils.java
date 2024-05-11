package com.example.demo.utils;


import com.example.demo.auth.dto.JwtSubjectDto;
import com.example.demo.common.AppContants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenUtils {
    private final Key siginingKey;
    private final JwtParser jwtParser;

    public JwtTokenUtils(
            @Value("${jwt.secret}")
            String jwtSecret
    ) {
        this.siginingKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtParser = Jwts
                .parserBuilder()
                .setSigningKey(this.siginingKey)
                .build();
    }

    public String generateToken(String userIdentifyUuid,
                                       String ipv4Address,
                                       String webType,
                                       String browser,
                                       String os
                                       ) throws JsonProcessingException {

        JwtSubjectDto jwtSubjectDto = JwtSubjectDto.builder()
                .tenantIdentifyUuid(userIdentifyUuid)
                .ipv4(ipv4Address)
                .browser(browser)
                .webType(webType)
                .os(os)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String jwtSubjectJson = objectMapper.writeValueAsString(jwtSubjectDto);

        Instant now = Instant.now();
        Claims jwtClaims = Jwts.claims()
                .setSubject(jwtSubjectJson)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(AppContants.ACCESS_TOKEN_EXPIRE_SECOND)));

        return Jwts.builder()
                .setClaims(jwtClaims)
                .signWith(this.siginingKey)
                .compact();
    }

    public boolean validate(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.warn("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.warn("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.warn("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.warn("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.warn("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    public Claims parseClaims(String token) {
        try {
            return jwtParser.parseClaimsJws(token).getBody();
        } catch (JwtException ex) {
            throw new RuntimeException("Failed to parse claims from token", ex);
        }
    }
}
