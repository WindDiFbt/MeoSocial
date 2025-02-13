package com.web.meosocial.domain.user.service.impl;

import com.web.meosocial.domain.user.model.User;
import com.web.meosocial.domain.user.model.UserDetailsImpl;
import com.web.meosocial.domain.user.model.UserInfo;
import com.web.meosocial.domain.user.repository.UserInfoRepository;
import com.web.meosocial.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInfoRepository userInfoRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> userOpt = userRepository.findByUserName(identifier);
        if (userOpt.isPresent()) {
            return new UserDetailsImpl(userOpt.get());
        }
        Optional<UserInfo> emailOpt = userInfoRepository.findByEmail(identifier);
        if (emailOpt.isPresent()) {
            return new UserDetailsImpl(emailOpt.get().getUser());
        }
        Optional<UserInfo> phoneOpt = userInfoRepository.findByPhoneNumber(identifier);
        if (phoneOpt.isPresent()) {
            return new UserDetailsImpl(phoneOpt.get().getUser());
        }
        throw new UsernameNotFoundException("User not found!");
    }

    public UserDetails loadUserByUserId(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UserDetailsImpl(user);
    }
}
