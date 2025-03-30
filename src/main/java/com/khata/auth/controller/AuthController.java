package com.khata.auth.controller;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.payload.JwtAuthRequest;
import com.khata.auth.payload.JwtAuthResponse;
import com.khata.auth.service.AuthService;
import com.khata.auth.service.UserService;
import com.khata.payload.ApiResponse;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UserDTO>> registerUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO registerUser = this.userService.createUser(userDTO);
        ApiResponse<UserDTO> response = new ApiResponse<>(registerUser, HttpStatus.CREATED.value(), "User Registered Successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/user-login")
    public ResponseEntity<ApiResponse<JwtAuthResponse>> loginUser(@Valid @RequestBody JwtAuthRequest jwtAuthRequest){
        JwtAuthResponse jwtAuthResponse = this.authService.authenticateUserAndGenerateToken(jwtAuthRequest);
        return ResponseEntity.ok(new ApiResponse<>(jwtAuthResponse, HttpStatus.OK.value()));
    }
}
