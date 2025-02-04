package com.web.meosocial.domain.user.service;

import com.web.meosocial.domain.user.dto.ChangePasswordDto;
import com.web.meosocial.domain.user.dto.UserDto;
import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.exception.RoleNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

    UserDto changePassword(ChangePasswordDto changePasswordDto);

    UserDto updateStatus(Long id, UserDto userDto);

    User getUserById(Long id);

    boolean existsByUserName(String username);

    void saveUser(User user);
}
