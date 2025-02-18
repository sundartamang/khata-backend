package com.khata.auth.service.impl;

import com.khata.auth.dto.UserDTO;
import com.khata.auth.repositories.UserRepo;
import com.khata.auth.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepo userRepo, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        return null;
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        return null;
    }

    @Override
    public Page<UserDTO> getUsers(Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserDTO> searchUserByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }
}
