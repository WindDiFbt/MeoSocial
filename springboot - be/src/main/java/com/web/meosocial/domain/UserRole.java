package com.web.meosocial.domain;

import com.web.meosocial.dto.UserRoleDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "userroles")
public class UserRole {
    @Id
    @Column(name = "user_roles_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    // Constructor to convert UserRoleDto to UserRole entity
    public UserRole(UserRoleDto userRoleDto) {
        if (userRoleDto != null) {
            this.id = userRoleDto.getId();
            if (userRoleDto.getUserId() != null) {
                User user = new User();
                user.setId(userRoleDto.getUserId());
                this.user = user;
            }
            if (userRoleDto.getRoleId() != null) {
                Role role = new Role();
                role.setId(userRoleDto.getRoleId());
                this.role = role;
            }
        }
    }
}