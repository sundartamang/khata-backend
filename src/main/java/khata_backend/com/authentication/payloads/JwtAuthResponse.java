package khata_backend.com.authentication.payloads;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    private String username;
    private String name;
}
