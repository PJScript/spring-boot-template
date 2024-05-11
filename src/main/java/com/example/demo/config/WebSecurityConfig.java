package com.example.demo.config;

import com.example.demo.filter.JwtExceptionFilter;
import com.example.demo.filter.JwtTokenFilter;
import com.example.demo.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final SecurityPath securityPath;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(auth -> auth

                .requestMatchers("*.css","*.html","*.js").permitAll()
                .requestMatchers(securityPath.getPublicPath()).permitAll()
                .anyRequest()
                .authenticated()
        ).addFilterBefore(
                new JwtTokenFilter(jwtTokenUtils, userDetailsService),
                AuthorizationFilter.class
        ).addFilterBefore(
                new JwtExceptionFilter(jwtTokenUtils, securityPath),
                AuthorizationFilter.class
        );

        return http.build();
    }


}
