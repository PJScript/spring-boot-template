package com.example.demo.auth.controller;

import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.mapper.AuthMapper;
import com.example.demo.auth.service.AuthService;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthMapper authMapper;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(
            HttpServletRequest httpServletRequest,
            @RequestBody
            LoginDto dto
    ) {
        return ResponseEntity.ok().body(authService.login(dto, httpServletRequest));
    }

}
