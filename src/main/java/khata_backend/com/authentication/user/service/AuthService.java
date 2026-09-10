package khata_backend.com.authentication.user.service;

import khata_backend.com.authentication.payloads.JwtAuthRequest;
import khata_backend.com.authentication.payloads.JwtAuthResponse;
import khata_backend.com.authentication.user.model.entity.Users;
import khata_backend.com.authentication.user.repository.UserRepo;
import khata_backend.com.exception.AuthenticationFailedException;
import khata_backend.com.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling user authentication.
 *
 * This service authenticates user credentials using Spring Security,
 * generates JWT tokens for successfully authenticated users, and
 * prepares the authentication response containing the JWT and user information.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;

    /**
     * Authenticates the user and generates a JWT token.
     *
     * Authentication flow:
     *
     * 1. Authenticate username and password.
     * 2. Load the authenticated user's details.
     * 3. Generate a JWT token.
     * 4. Load the user entity from the database.
     * 5. Convert the entity into UsersDTO.
     * 6. Return the JWT and user information.
     */
    public JwtAuthResponse authenticateUser(JwtAuthRequest request) {

        // Authenticate the user's credentials using Spring Security.
        authenticate(request.getUsername(), request.getPassword());

        // Load UserDetails after successful authentication.
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

        // Generate JWT for the authenticated user.
        String token = jwtTokenService.generateToken(userDetails);

        // Load the complete user entity from the database.
        Users user = userRepo.findByEmail(request.getUsername())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        // Create the authentication response.
        JwtAuthResponse response = new JwtAuthResponse();

        response.setToken(token);
        response.setUsername(user.getEmail());
        response.setName(user.getName());

        return response;
    }

    /**
     * Authenticates the supplied username and password.
     *
     * AuthenticationManager delegates authentication to
     * the configured Spring Security authentication provider.
     *
     * A generic error message is returned when authentication
     * fails so that sensitive authentication information is not exposed.
     */
    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            log.debug("User authentication successful for: {}", username);

        } catch (BadCredentialsException e) {
            log.warn("Authentication failed for user: {}", username);
            throw new AuthenticationFailedException("Invalid username or password");
        }
    }
}