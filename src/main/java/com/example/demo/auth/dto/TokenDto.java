package com.example.demo.auth.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TokenDto {
    private String accessToken;
    private LocalDateTime issuedDate;
    private LocalDateTime expiredDate;
    private Integer expiredSecond;
}
