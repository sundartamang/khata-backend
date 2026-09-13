package khata_backend.com.authentication.user.service.impl;

import khata_backend.com.authentication.user.mapper.UserMapper;
import khata_backend.com.authentication.user.model.dto.UsersDTO;
import khata_backend.com.authentication.user.model.entity.Users;
import khata_backend.com.authentication.user.repository.UserRepo;
import khata_backend.com.authentication.user.service.UserService;
import khata_backend.com.exception.DuplicateResourceException;
import khata_backend.com.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer — contains pure business logic for user operations.
 *
 * <p>Input validation and use-case logging live in the Facade layer.
 * This layer only logs domain/business-level events (e.g. duplicate email,
 * successful persistence).
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    /**
     * Registers a new user after checking for duplicate email and encoding the password.
     */
    @Override
    public UsersDTO registerNewUser(UsersDTO userDto) {
        return saveNewUser(userDto, "registerNewUser");
    }


    /**
     * Creates a new user (admin/internal path — no duplicate-email check).
     */
    @Override
    public UsersDTO createUser(UsersDTO userDto) {
        return saveNewUser(userDto, "createUser");
    }

    /**
     * Updates an existing user's mutable fields.
     */
    @Override
    public UsersDTO updateUser(UsersDTO userDto, Integer userId) {
        Users user = findUserById(userId);

        if (userDto.getName() != null && !userDto.getName().isEmpty()) {
            user.setName(userDto.getName());
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        Users updatedUser = userRepo.save(user);
        log.info("[SERVICE] updateUser — user updated with id: {}", userId);

        return userMapper.toDto(updatedUser);
    }

    /**
     * Retrieves a single user by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public UsersDTO getUserDetail(Integer userId) {
        Users user = findUserById(userId);

        log.debug("[SERVICE] getUserDetail — found user email: {} for id: {}", user.getEmail(), userId);
        return userMapper.toDto(user);
    }

    /**
     * Retrieves all users.
     */
    @Override
    @Transactional(readOnly = true)
    public List<UsersDTO> getAllUsers() {
        List<Users> users = userRepo.findAll();
        log.debug("[SERVICE] getAllUsers — retrieved {} user(s) from database", users.size());

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a user by ID.
     */
    @Override
    public void deleteUser(Integer userId) {
        Users user = findUserById(userId);

        userRepo.delete(user);
        log.info("[SERVICE] deleteUser — user deleted with id: {} email: {}", userId, user.getEmail());
    }

    /**
     * Checks whether a user with the given email already exists.
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        boolean exists = userRepo.existsByEmail(email);
        log.debug("[SERVICE] existsByEmail — email: {} exists: {}", email, exists);
        return exists;
    }


    // Private helper methods
    private Users findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> {
                    log.error("[SERVICE] User not found with id: {}", userId);
                    return new ResourceNotFoundException(
                            "User not found with ID: " + userId
                    );
                });
    }

    private UsersDTO saveNewUser(UsersDTO userDto, String operationName) {
        if (userRepo.existsByEmail(userDto.getEmail())) {
            log.warn("[SERVICE] {} - email already in use", operationName);
            throw new DuplicateResourceException("User already exists with email: " + userDto.getEmail());
        }

        Users user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Users savedUser = userRepo.save(user);
        log.info("[SERVICE] {} - user persisted with id: {}", operationName, savedUser.getId());
        return userMapper.toDto(savedUser);
    }
}