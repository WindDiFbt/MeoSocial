package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.domain.user.model.Role;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.model.UserRole;
import com.web.meosocial.domain.user.repository.RoleRepository;
import com.web.meosocial.domain.user.repository.UserRepository;
import com.web.meosocial.domain.user.repository.UserRoleRepository;
import com.web.meosocial.domain.user.service.UserRoleService;
import com.web.meosocial.util.UUID64Generator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    private final UUID64Generator uuid64Generator = new UUID64Generator();

    @Transactional
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
