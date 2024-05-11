package com.example.demo.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtSubjectDto {
    private String tenantIdentifyUuid;
    private String ipv4;
    private String browser;
    private String os;
    private String webType;
}
