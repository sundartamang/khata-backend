package com.khata.auth.controller;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.service.UserService;
import com.khata.payload.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO){
        UserDTO user = this.userService.createUser(userDTO);
        return new ResponseEntity<UserDTO>(user, HttpStatus.CREATED);
    }

    @PostMapping("/list")
    public ResponseEntity<Page<UserDTO>> getUsers(Pageable pageable){
        Page<UserDTO> userDTOPage = this.userService.getUsers(pageable);
        return ResponseEntity.ok(userDTOPage);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable Integer userId){
        UserDTO updatedUser = this.userService.updateUser(userDTO, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer userId){
        UserDTO userDTO = this.userService.getUserById(userId);
        return ResponseEntity.ok(userDTO);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Integer userId){
        this.userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User deleted successfully", true));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<UserDTO>> searchUserByFullName(@RequestParam String keyword, Pageable pageable){
        Page<UserDTO> userDTOPage = this.userService.searchUserByName(keyword, pageable);
        return ResponseEntity.ok(userDTOPage);
    }
}
