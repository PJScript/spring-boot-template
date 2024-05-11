package com.example.demo.auth.repo;

import com.example.demo.auth.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TenantRepo extends JpaRepository<Tenant, Long> {
    Optional<Tenant> findByUuid(String uuid);
    Optional<Tenant> findByEmail(String email);
}
