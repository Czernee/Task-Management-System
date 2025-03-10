package com.example.task_management_system.service.impl;

import com.example.task_management_system.models.Role;
import com.example.task_management_system.repository.RoleRepository;
import com.example.task_management_system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void initRoles() {
        if (!roleRepository.existsByName("USER")) {
            Role roleUser = new Role("USER");
            roleRepository.save(roleUser);
        }

        if (!roleRepository.existsByName("ADMIN")) {
            Role roleAdmin = new Role("ADMIN");
            roleRepository.save(roleAdmin);
        }
    }
}
