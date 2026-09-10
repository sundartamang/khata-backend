package khata_backend.com.authentication.user.controller;

import jakarta.validation.Valid;
import khata_backend.com.authentication.user.facade.UserFacade;
import khata_backend.com.authentication.user.model.dto.UsersDTO;
import khata_backend.com.common.BaseResponse;
import khata_backend.com.common.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserFacade userFacade;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<UsersDTO>> getUserDetail(@PathVariable("id") Integer userId) {
        return ResponseBuilder.build(userFacade.getUserDetail(userId));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<UsersDTO>>> getAllUsers() {
        return ResponseBuilder.build(userFacade.getAllUsers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UsersDTO>> updateUser(@Valid @RequestBody UsersDTO usersDTO, @PathVariable("id") Integer userId) {
        return ResponseBuilder.build(userFacade.updateUser(usersDTO, userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable("id") Integer userId) {
        return ResponseBuilder.build(userFacade.deleteUser(userId));
    }
}

