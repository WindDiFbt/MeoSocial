package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.RoleDto;
import com.web.meosocial.exception.RoleNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {
    RoleDto getInstance (String roleName) throws RoleNotFoundException;
}
