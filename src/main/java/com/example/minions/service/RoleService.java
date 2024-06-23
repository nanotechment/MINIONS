package com.example.minions.service;

import com.example.minions.entity.Roles;

public interface RoleService {
    public Roles save(Roles role);
    public Roles findByRoleName(String roleName);
}