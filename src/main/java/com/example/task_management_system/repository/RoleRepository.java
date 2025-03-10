package com.example.task_management_system.repository;

import com.example.task_management_system.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);

    boolean existsByName(String name);
}
