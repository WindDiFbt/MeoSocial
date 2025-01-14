package com.web.meosocial.service.user.impl;

import com.web.meosocial.constant.Enums;
import com.web.meosocial.domain.user.User;
import com.web.meosocial.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserInfoDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    public UserInfoDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(username);
        if (user.isPresent()) {
            if (!user.get().getUserStatus().equals(Enums.UserStatus.NOT_AVAILABLE.getValue())) {
                return user.get();
            }
            throw new IllegalArgumentException("User account is not available");
        }
        throw new UsernameNotFoundException("User not found: " + username);
    }
}
