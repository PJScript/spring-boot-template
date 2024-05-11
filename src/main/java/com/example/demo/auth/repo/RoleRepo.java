package com.example.demo.auth.repo;


import com.example.demo.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role,Long> {
    Optional<Role> findRoleByName(String name);
}
