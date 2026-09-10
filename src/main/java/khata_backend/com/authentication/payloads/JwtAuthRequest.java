package khata_backend.com.authentication.payloads;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JwtAuthRequest {

    @NotBlank(message = "Username/Email cannot be blank")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}