package com.web.meosocial.auth;

import com.web.meosocial.domain.user.model.UserDetailsImpl;
import com.web.meosocial.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    /**
     * Retrieves the current authenticated user's ID.
     *
     * @return {@code Long} The ID of the authenticated user.
     * @throws UnauthorizedException If the user is not authenticated.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) authentication.getPrincipal()).getId();
        }
        throw new UnauthorizedException("Unauthorized");
    }

    /**
     * Validates whether a user is authenticated.
     *
     * @throws UnauthorizedException If the user is not authenticated.
     */
    public void validateUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl)) {
            throw new UnauthorizedException("Unauthorized");
        }
    }
}
