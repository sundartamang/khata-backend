package khata_backend.com.authentication.user.controller;

import jakarta.validation.Valid;
import khata_backend.com.authentication.payloads.JwtAuthRequest;
import khata_backend.com.authentication.payloads.JwtAuthResponse;
import khata_backend.com.authentication.user.facade.UserFacade;
import khata_backend.com.authentication.user.model.dto.UsersDTO;
import khata_backend.com.common.BaseResponse;
import khata_backend.com.common.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class authController {

    private final UserFacade userFacade;

    @PostMapping("/register")
    public ResponseEntity<BaseResponse<UsersDTO>> registerUser(
            @Valid @RequestBody UsersDTO usersDTO) {

        return ResponseBuilder.build(userFacade.registerNewUser(usersDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<JwtAuthResponse>> loginUser(
            @Valid @RequestBody JwtAuthRequest request) {

        return ResponseBuilder.build(userFacade.loginUser(request));
    }
}
