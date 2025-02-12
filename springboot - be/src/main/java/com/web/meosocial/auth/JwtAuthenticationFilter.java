package com.web.meosocial.auth;

import com.web.meosocial.auth.service.RedisService;
import com.web.meosocial.domain.user.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisService redisService;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getToken(request);
            if (jwt != null && jwtUtils.validateToken(jwt)) {
                Long userId = jwtUtils.claimUserId(jwt);
                String storedJwt = redisService.getToken(userId);
                if (storedJwt != null && storedJwt.equals(jwt)) {
                    UserDetails userDetails = userDetailsService.loadUserByUserId(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    authentication.setDetails(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.error("Invalid token or token not found in Redis.");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
