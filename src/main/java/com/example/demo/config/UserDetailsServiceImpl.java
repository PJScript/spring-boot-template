package com.example.demo.config;


import com.example.demo.auth.entity.Tenant;
import com.example.demo.auth.repo.TenantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TenantRepo tenantRepo;

    @Override
    public UserDetailsImpl loadUserByUsername(String uuid) throws UsernameNotFoundException {
        Tenant tenant = tenantRepo.findByUuid(uuid)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with UUID: " + uuid));

        return UserDetailsImpl.builder()
                .tenant(tenant)
                .build();
    }
}
