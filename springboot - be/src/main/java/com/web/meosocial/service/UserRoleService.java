package com.web.meosocial.service;

import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {
    void assignRole(Long userId, String roleName);
}
