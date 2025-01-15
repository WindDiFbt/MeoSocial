package com.web.meosocial.domain.user.service;

import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {
    void assignRole(Long userId, String roleName);
}
