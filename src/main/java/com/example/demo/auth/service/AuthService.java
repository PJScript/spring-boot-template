package com.example.demo.auth.service;

import com.example.demo.auth.dto.LoginDto;
import com.example.demo.auth.dto.SignUpDto;
import com.example.demo.auth.dto.TokenDto;
import com.example.demo.auth.entity.Role;
import com.example.demo.auth.entity.Tenant;
import com.example.demo.auth.repo.RoleRepo;
import com.example.demo.auth.repo.TenantRepo;
import com.example.demo.common.AppContants;
import com.example.demo.exception.GlobalErroCode;
import com.example.demo.exception.GlobalExceptionHandler;
import com.example.demo.utils.ConnectUtils;
import com.example.demo.utils.JwtTokenUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final RoleRepo roleRepo;
    private final JwtTokenUtils jwtTokenUtils;
    private final TenantRepo tenantRepo;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public TokenDto login(LoginDto dto, HttpServletRequest httpServletRequest) {
        String ip = ConnectUtils.getIp(httpServletRequest);
        String browser = ConnectUtils.getBrowser(httpServletRequest);
        String os = ConnectUtils.getOs(httpServletRequest);
        String webType = ConnectUtils.getWebType(httpServletRequest);

        System.out.println("------- Request Info -------");
        System.out.println("email: " + dto.getEmail());
//        System.out.println("password: " + dto.getPassword());
        System.out.println("ip: " + ip);
        System.out.println("browser: " + browser);
        System.out.println("os: " + os);
        System.out.println("webType: " + webType);
        System.out.println("-----------------------------");


        Tenant tenant = tenantRepo.findByEmail(dto.getEmail()).orElseThrow(() -> new GlobalExceptionHandler(GlobalErroCode.AUTHENTICATION_FAILED));
        if (!passwordEncoder.matches(dto.password, tenant.getPassword())) {
            throw new GlobalExceptionHandler(GlobalErroCode.AUTHENTICATION_FAILED);
        }

        try {
            return TokenDto.builder()
                    .accessToken(jwtTokenUtils.generateToken(tenant.getUuid(), ip, webType, browser, os))
                    .issuedDate(LocalDateTime.now())
                    .expiredSecond(AppContants.ACCESS_TOKEN_EXPIRE_SECOND)
                    .expiredDate(LocalDateTime.now().plusSeconds(AppContants.ACCESS_TOKEN_EXPIRE_SECOND))
                    .build();
        } catch (JsonProcessingException jsonProcessingException) {
            log.warn(jsonProcessingException.getMessage());
            throw new GlobalExceptionHandler(GlobalErroCode.AUTHENTICATION_FAILED);
        }

    }



}
