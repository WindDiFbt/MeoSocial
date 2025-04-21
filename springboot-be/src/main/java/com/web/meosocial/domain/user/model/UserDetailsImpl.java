package com.web.meosocial.domain.user.model;

import com.web.meosocial.constant.Enums;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean isAccountNonLocked;
    private final boolean isAccountNonExpired;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
        this.isAccountNonLocked = true;
        this.isAccountNonExpired = true;
        this.isCredentialsNonExpired = true;
        this.isEnabled = !user.getUserStatus().equals(Enums.UserStatus.NOT_AVAILABLE.getValue());
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
