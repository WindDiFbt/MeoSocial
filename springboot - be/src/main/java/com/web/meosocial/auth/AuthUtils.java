package com.web.meosocial.auth;

import com.web.meosocial.domain.user.model.UserDetailsImpl;
import com.web.meosocial.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

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

    public String getDeviceId(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        String ipAddress = request.getRemoteAddr();
        return DigestUtils.md5DigestAsHex((userAgent + ipAddress).getBytes()); // Hash thành ID duy nhất
    }

}
