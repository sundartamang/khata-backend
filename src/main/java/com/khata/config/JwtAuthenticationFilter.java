package com.khata.config;

import com.khata.auth.service.JwtTokenService;
import com.khata.exceptions.JwtTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtAuthenticationFilter is a custom filter that processes JWT authentication tokens in HTTP requests.
 * It extracts the token from the request, validates it, and sets the authentication context for the user if valid.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserDetailsService userDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extractToken(request);
            if (token != null) {
                String username = jwtTokenService.getUsernameFromToken(token);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    setAuthentication(username, token, request);
                }
            }

            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException | MalformedJwtException | io.jsonwebtoken.SignatureException | IllegalArgumentException ex) {
            log.error("JWT exception: {}", ex.getMessage());
            handleJwtException(response, ex);
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request the HTTP request
     * @return the extracted JWT token, or null if no token is found
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * Sets the authentication context if the JWT token is valid.
     * Loads user details, validates the token, and creates an authentication token
     * to set in the SecurityContextHolder.
     *
     * @param username the username extracted from the token
     * @param token the JWT token
     * @param request the HTTP request
     */
    private void setAuthentication(String username, String token, HttpServletRequest request) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (jwtTokenService.validateToken(token, userDetails)) {
            List<GrantedAuthority> authorities = jwtTokenService.getRolesFromToken(token).stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            log.info("Successfully authenticated user: {}", username);
        } else {
            log.warn("JWT token validation failed for user: {}", username);
        }
    }

    /**
     * Handles JWT-related exceptions by setting a 401 Unauthorized response
     * with a JSON error message based on the exception type.
     *
     * @param response the HTTP response to write to
     * @param ex the JWT-related exception
     * @throws IOException if writing to the response fails
     */
    private void handleJwtException(HttpServletResponse response, Exception ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String message = "Invalid or expired JWT token";

        if (ex instanceof ExpiredJwtException) {
            message = "JWT token has expired";
        } else if (ex instanceof MalformedJwtException) {
            message = "Malformed JWT token";
        } else if (ex instanceof io.jsonwebtoken.SignatureException) {
            message = "Invalid JWT signature";
        } else if (ex instanceof IllegalArgumentException) {
            message = "JWT token is missing or invalid";
        }

        String jsonResponse = String.format(
                "{\"statusCode\":%d,\"message\":\"%s\"}",
                HttpStatus.UNAUTHORIZED.value(),
                message
        );

        response.getWriter().write(jsonResponse);
    }
}