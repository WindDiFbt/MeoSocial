package com.web.meosocial.domain.user.repository;

import com.web.meosocial.domain.user.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
