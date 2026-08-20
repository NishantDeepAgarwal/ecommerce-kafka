package com.order.service.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        response.setHeader("error-reason", "Unauthorized");
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        JwtErrorResponse errorResponse = new JwtErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorResponse.setMessage(authException.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
