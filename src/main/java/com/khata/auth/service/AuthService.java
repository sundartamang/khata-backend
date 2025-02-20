package com.khata.auth.service;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.entity.User;
import com.khata.auth.payload.JwtAuthRequest;
import com.khata.auth.payload.JwtAuthResponse;
import com.khata.auth.repositories.UserRepo;
import com.khata.exceptions.ApiException;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public AuthService(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            JwtTokenService jwtTokenService,
            UserRepo userRepo,
            ModelMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    public JwtAuthResponse authenticateUserAndGenerateToken(JwtAuthRequest jwtAuthRequest) {
        authenticateUserCredentials(jwtAuthRequest.getUsername(), jwtAuthRequest.getPassword());

        UserDetails userDetails = loadUserDetailsByUsername(jwtAuthRequest.getUsername());
        String token = generateJwtTokenForUser(userDetails);
        UserDTO userDTO = mapUserEntityToDTO(findUserEntityByEmail(jwtAuthRequest.getUsername()));

        return new JwtAuthResponse(token, userDTO);
    }

    private void authenticateUserCredentials(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid username or password");
        }
    }

    private UserDetails loadUserDetailsByUsername(String username) {
        return userDetailsService.loadUserByUsername(username);
    }

    private String generateJwtTokenForUser(UserDetails userDetails) {
        return jwtTokenService.generateToken(userDetails);
    }

    private User findUserEntityByEmail(String email) {
        return userRepo.findByEmail(email).orElseThrow(
                () -> new ApiException("User not found"));
    }

    private UserDTO mapUserEntityToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
