package com.web.meosocial.dto;

import com.web.meosocial.domain.UserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRoleDto {
    private Long id;
    private Long userId;
    private Integer roleId;

    // Constructor to map from UserRole entity to UserRoleDto
    public UserRoleDto(UserRole userRole) {
        if (userRole != null) {
            this.id = userRole.getId();
            this.userId = userRole.getUser() != null ? userRole.getUser().getId() : null;
            this.roleId = userRole.getRole() != null ? userRole.getRole().getId() : null;
        }
    }
}
