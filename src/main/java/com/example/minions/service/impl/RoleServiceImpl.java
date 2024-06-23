package com.example.minions.service.impl;

import com.example.minions.entity.Roles;
import com.example.minions.repository.RoleRepository;
import com.example.minions.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;
    @Override
    public Roles save(Roles role) {
        return roleRepository.save(role);
    }
    @Override
    public Roles findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
