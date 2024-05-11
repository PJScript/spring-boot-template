package com.example.demo.auth.dto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MeDto {
    private String uuid;
    private String email;
    private String lastAccessIpv4Address;
    private LocalDateTime lastPasswordUpdateDate;
    private LocalDateTime lastLoginDate;
    private String roleName;
}
