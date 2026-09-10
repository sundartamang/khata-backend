package khata_backend.com.authentication.user.service;

import khata_backend.com.authentication.user.model.dto.UsersDTO;

import java.util.List;

public interface UserService {
    UsersDTO registerNewUser(UsersDTO user);
    UsersDTO createUser(UsersDTO user);
    UsersDTO updateUser(UsersDTO user, Integer userId);
    UsersDTO getUserDetail(Integer userId);
    List<UsersDTO> getAllUsers();
    void deleteUser(Integer userId);
    boolean existsByEmail(String email);
}