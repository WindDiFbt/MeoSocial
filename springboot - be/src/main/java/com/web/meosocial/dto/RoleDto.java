package com.web.meosocial.dto;

import com.web.meosocial.domain.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDto {
    private Integer id;
    private String name;

    // Constructor to map from Role entity to RoleDto
    public RoleDto(Role role) {
        if (role != null) {
            this.id = role.getId();
            this.name = role.getName();
        }
    }
}
