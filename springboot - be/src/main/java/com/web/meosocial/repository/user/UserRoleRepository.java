package com.web.meosocial.repository.user;

import com.web.meosocial.domain.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
