package com.example.demo.config;

import com.example.demo.auth.entity.Tenant;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@Builder
public class UserDetailsImpl implements UserDetails {
    private Tenant tenant;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(
                new SimpleGrantedAuthority(tenant.getRole().getName()));
    }


    @Override
    public String getPassword() {
        return tenant.getPassword();
    }

    @Override
    public String getUsername() {
        return tenant.getUuid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return tenant.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return tenant.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return tenant.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return tenant.isEnabled();
    }
}