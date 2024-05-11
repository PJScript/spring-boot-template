package com.example.demo.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpDto {
    private String email;
    private String password;

    // 성별, 나이, 지역 등등
}
