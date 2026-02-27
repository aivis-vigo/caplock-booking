package com.caplock.booking.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        if (wantsHtml(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            request.getRequestDispatcher("/ui/error").forward(request, response);
            return;
        }

        Throwable cause = authException.getCause();
        if (isJwtAuthenticationError(authException, cause)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "401 Unauthorized: " + authException.getMessage());
        } else if (authException instanceof BadCredentialsException || authException instanceof InsufficientAuthenticationException) {
            response.sendError(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION, "203 Non-Authoritative Information: " + authException.getMessage());
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "500 Problem with request: " + authException.getMessage());
        }
    }

    private boolean isJwtAuthenticationError(AuthenticationException authException, Throwable cause) {
        if (cause instanceof ExpiredJwtException) {
            return true;
        }

        if (cause instanceof JwtException) {
            return true;
        }

        // Check the exception class name as fallback for authorization errors
        return authException.getClass().getSimpleName().contains("Jwt")
                || authException.getClass().getSimpleName().contains("Authorization");
    }

    private boolean wantsHtml(HttpServletRequest request) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("text/html")) {
            return true;
        }

        String uri = request.getRequestURI();
        return uri != null && uri.startsWith("/ui/");
    }
}
