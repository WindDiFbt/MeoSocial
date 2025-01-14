package com.web.meosocial.service.user.impl;

import com.web.meosocial.domain.user.Role;
import com.web.meosocial.domain.user.User;
import com.web.meosocial.domain.user.UserRole;
import com.web.meosocial.repository.user.RoleRepository;
import com.web.meosocial.repository.user.UserRepository;
import com.web.meosocial.repository.user.UserRoleRepository;
import com.web.meosocial.service.user.UserRoleService;
import com.web.meosocial.util.UUID64Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    private final UUID64Generator uuid64Generator = new UUID64Generator();

    @Override
    public void assignRole(Long userId, String roleType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleType)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        UserRole userRole = new UserRole();
        userRole.setId(uuid64Generator.generateUUID64());
        userRole.setUser(user);
        userRole.setRole(role);
        userRoleRepository.save(userRole);
    }
}
