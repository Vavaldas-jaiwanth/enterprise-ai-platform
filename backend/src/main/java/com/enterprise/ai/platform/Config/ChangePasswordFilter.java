package com.enterprise.ai.platform.Config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.enterprise.ai.platform.Model.CustomUserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ChangePasswordFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    @org.springframework.context.annotation.Lazy
    private HandlerExceptionResolver exceptionResolver;

    private final RequestMatcher allowedEndpoints =
        new OrRequestMatcher(
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/change-password"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/logout"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/refresh"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/me"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/login"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/auth/forgot-password")
        );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return allowedEndpoints.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated() || 
                authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("Unauthorized access");
            }

            if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
                filterChain.doFilter(request, response);
                return;
            }   

            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

            if (user.isMustChangePassword()) {
                throw new AccessDeniedException("Password change required");
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            exceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
