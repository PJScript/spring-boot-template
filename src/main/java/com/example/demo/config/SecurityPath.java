package com.example.demo.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Getter
@NoArgsConstructor
@Component
public class SecurityPath {
    String[] publicPath = {
            "/api/v1/auth/login",
            "/api/v1/auth/**",
            "/login",
            "/healthcheck"
    };
}
