package com.khata.config;

import com.khata.auth.service.JwtTokenService;
import com.khata.exceptions.JwtTokenException;
import com.khata.payload.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JwtAuthenticationFilter is a custom filter that processes JWT authentication tokens in HTTP requests.
 * It extracts the token from the request, validates it, and sets the authentication context for the user if valid.
 * This filter is invoked on each request to ensure that the user is authenticated before accessing secured resources.
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtTokenService jwtTokenService, UserDetailsService userDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * This method intercepts the request, extracts the JWT token, validates it, and sets the authentication
     * context if the token is valid.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @param filterChain the filter chain that allows the request to proceed
     * @throws ServletException if the request could not be processed
     * @throws IOException if an I/O error occurs during the request processing
     */
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

        } catch (ExpiredJwtException | MalformedJwtException | io.jsonwebtoken.SignatureException  | IllegalArgumentException ex) {
//            throw new JwtTokenException(ex.getMessage());

            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        }
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request the HTTP request
     * @return the extracted JWT token, or null if no token is found
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
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
        }
    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"status\": " + status.value() + ", \"message\": \"" + message + "\"}");
    }

}
