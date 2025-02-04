package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.dto.RoleDto;
import com.web.meosocial.domain.user.repository.RoleRepository;
import com.web.meosocial.domain.user.service.RoleService;
import com.web.meosocial.exception.RoleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public RoleDto getInstance(String roleName) throws RoleNotFoundException {
        switch (roleName) {
            case "admin" -> {
                return new RoleDto(roleRepository.findByName(Enums.RoleNames.ROLE_ADMIN.toString()));
            }
            case "user" -> {
                return new RoleDto(roleRepository.findByName(Enums.RoleNames.ROLE_USER.toString()));
            }
            case "moderator" -> {
                return new RoleDto(roleRepository.findByName(Enums.RoleNames.ROLE_MODERATOR.toString()));
            }
            default -> {
                throw new RoleNotFoundException("Role not found: " + roleName);
            }
        }
    }
}
