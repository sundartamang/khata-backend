package com.khata.auth.payload;

import com.khata.auth.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class JwtAuthResponse {
    private String token;
    private UserDTO userDTO;
}
