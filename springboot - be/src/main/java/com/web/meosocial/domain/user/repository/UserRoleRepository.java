package com.web.meosocial.domain.user.repository;

import com.web.meosocial.domain.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
