package com.web.meosocial.service;

import com.web.meosocial.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAll();

}
