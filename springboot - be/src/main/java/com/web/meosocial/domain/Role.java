package com.web.meosocial.domain;

import com.web.meosocial.dto.RoleDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 50)
    private String name;

    @OneToMany(mappedBy = "role")
    private Set<com.web.meosocial.domain.UserRole> userroles = new LinkedHashSet<>();

    // Constructor to convert RoleDto to Role entity
    public Role(RoleDto roleDto) {
        if (roleDto != null) {
            this.id = roleDto.getId();
            this.name = roleDto.getName();
        }
    }
}