package com.khata.auth.service.impl;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.entity.User;
import com.khata.auth.exceptions.EmailAlreadyExistsException;
import com.khata.auth.repositories.UserRepo;
import com.khata.auth.service.UserService;
import com.khata.exceptions.ResourceNotFoundException;
import com.khata.utils.AppConstants;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder encoder;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper, BCryptPasswordEncoder encoder) {
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
        logger.info("User created with email: {}", userDTO.getEmail());
        return convertDTO(savedUser);
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", userId));
        user.setFullName(userDTO.getFullName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        if(userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()){
            user.setPassword(encodePassword(userDTO.getPassword()));
        }
        User updateUser = this.userRepo.save(user);
        logger.info("User updated with ID: {}", userId);
        return convertDTO(updateUser);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", userId));
        return convertDTO(user);
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        Page<User> users = this.userRepo.findAll(pageable);
        return users.map(this::convertDTO);
    }

    @Override
    public Page<UserDTO> searchUserByName(String name, Pageable pageable) {
        Page<User> users = this.userRepo.findByFullNameContainingIgnoreCase(name, pageable);
        return users.map(this::convertDTO);
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
        User user = this.userRepo.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("User", "id", userId));
        logger.info("User deleted with ID: {}", userId);
        this.userRepo.delete(user);
    }

    // helper methods
    private String encodePassword(String rawPassword){
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

    private boolean isEmailValid(String email) { return email != null && email.matches(AppConstants.EMAIL_REGEX);}
}
