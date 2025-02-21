package com.khata.auth.controller;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.payload.JwtAuthRequest;
import com.khata.auth.payload.JwtAuthResponse;
import com.khata.auth.service.AuthService;
import com.khata.auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    public AuthController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO registerUser = this.userService.createUser(userDTO);
        return new ResponseEntity<UserDTO>(registerUser, HttpStatus.CREATED);
    }

    @PostMapping("/user-login")
    public ResponseEntity<JwtAuthResponse> loginUser(@Valid @RequestBody JwtAuthRequest jwtAuthRequest){
        JwtAuthResponse jwtAuthResponse = this.authService.authenticateUserAndGenerateToken(jwtAuthRequest);
        return ResponseEntity.ok(jwtAuthResponse);
    }
}
