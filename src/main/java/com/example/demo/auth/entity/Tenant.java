package com.example.demo.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Builder.Default
    private String uuid = UUID.randomUUID().toString();
    private String email;
    @Column(name = "last_access_ipv4_address")
    private String lastAccessIpv4Address;
    private String lastAccessBrowser;
    private String password;

    // 계정이 잠겨있는지 확인하기 위해사용. 비정상 접근, 이용 등을 이유로 이 값을 변경함.
    @Builder.Default
    private boolean isAccountNonLocked = true;
    // 로그인 실패 횟수 카운팅
    @Column(name = "login_failed_count")
    @Builder.Default
    private Integer loginFailedCount = 0;

    // 사용자의 인증 정보(비밀번호 등)가 만료되었는지 검사하는 용도. 일정 주기 비밀번호 재설정 알림을 위해 사용.
    @Builder.Default
    private boolean isCredentialsNonExpired = true;
    // 비밀번호 마지막 변경일
    private LocalDateTime lastPasswordUpdateDate;

    // 계정이 만료 되었는지 여부를 확인하기 위한 필드. 기간 한정 테스트 계정 등을 위해 사용
    @Builder.Default
    private boolean isAccountNonExpired = true;
    // 계정 만료일
    private LocalDateTime accountExpiredDate;

    // 계정이 활성화 상태인지 확인합니다. 휴면 계정 정책을 도입하여 장기간 활동이 없는 계정을 비활성화.
    @Builder.Default
    private boolean isEnabled = true;
    // 마지막 로그인 날짜
    private LocalDateTime lastLoginDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @Builder.Default
    private Role role = Role.builder()
            .id(1L)
            .name("USER")
            .build();
}
