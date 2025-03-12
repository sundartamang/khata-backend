package com.khata.auth.service.impl;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.entity.User;
import com.khata.auth.exceptions.EmailAlreadyExistsException;
import com.khata.auth.repositories.UserRepo;
import com.khata.auth.service.UserService;
import com.khata.exceptions.ResourceNotFoundException;
import com.khata.utils.AppConstants;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
    }

    @Override
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        checkEmailIfExists(userDTO.getEmail());
        User user = convertToEntity(userDTO);
        user.setPassword(encodePassword(userDTO.getPassword()));
        User savedUser = this.userRepo.save(user);
        log.info("User created with email: {}", userDTO.getEmail());
        return convertDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        User user = getUserEntityById(userId);
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setPassword(encodePassword(userDTO.getPassword()));
        }
        User updateUser = this.userRepo.save(user);
        log.info("User updated with ID: {}", userId);
        return convertDTO(updateUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Integer userId) {
        User user = getUserEntityById(userId);
        return convertDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> getUsers(Pageable pageable) {
        Page<User> users = this.userRepo.findAll(pageable);
        return users.map(this::convertDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> searchUserByName(String name, Pageable pageable) {
        Page<User> users = this.userRepo.findByFullNameContainingIgnoreCase(name, pageable);
        return users.map(this::convertDTO);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = getUserEntityById(userId);
        this.userRepo.delete(user);
        log.info("User deleted with ID: {}", userId);
    }


    private User getUserEntityById(Integer userId) {
        return userRepo.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId));
    }

    private String encodePassword(String rawPassword){
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return this.encoder.encode(rawPassword);
    }

    private UserDTO convertDTO(User user){
        return this.modelMapper.map(user, UserDTO.class);
    }

    private User convertToEntity(UserDTO userDTO){
        return this.modelMapper.map(userDTO, User.class);
    }

    private void checkEmailIfExists(String email) {
        if (!isEmailValid(email)) {throw new IllegalArgumentException("Invalid email format");}

        if (this.userRepo.findByEmail(email).isPresent()) { throw new EmailAlreadyExistsException("Email", "email", email);}
    }

    private boolean isEmailValid(String email) {
        return email != null && !email.isEmpty() && email.matches(AppConstants.EMAIL_REGEX);
    }
}
