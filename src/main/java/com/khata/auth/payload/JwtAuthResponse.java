package com.khata.auth.payload;

import com.khata.auth.dto.UserDTO;
import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    private UserDTO userDTO;
}
