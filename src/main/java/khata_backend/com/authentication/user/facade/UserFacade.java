package khata_backend.com.authentication.user.facade;

import khata_backend.com.authentication.payloads.JwtAuthRequest;
import khata_backend.com.authentication.payloads.JwtAuthResponse;
import khata_backend.com.authentication.user.model.dto.UsersDTO;
import khata_backend.com.authentication.user.service.AuthService;
import khata_backend.com.authentication.user.service.UserService;
import khata_backend.com.common.BaseResponse;
import khata_backend.com.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Facade layer — orchestrates logging, input validation, service delegation,
 * and response building for all user-related use cases.
 *
 * <p>This is the single entry point that controllers call. It ensures:
 * <ul>
 *   <li>Entry and exit logs are captured at the use-case boundary</li>
 *   <li>Input validation runs before the service is ever invoked</li>
 *   <li>Responses are wrapped in a consistent {@link BaseResponse}</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacade {

    private final UserService userService;
    private final AuthService authService;

    // -------------------------------------------------------------------------
    // Register
    // -------------------------------------------------------------------------

    /**
     * Registers a new user.
     *
     * <p>Validates input, delegates to {@link UserService#registerNewUser(UsersDTO)},
     * and wraps the result in a {@link BaseResponse}.
     *
     * @param usersDTO the registration payload
     * @return standardized response containing the saved user DTO
     */
    public BaseResponse<UsersDTO> registerNewUser(UsersDTO usersDTO) {
        requireUserPayload(usersDTO);

        UsersDTO saved = userService.registerNewUser(usersDTO);

        return BaseResponse.<UsersDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("User registered successfully")
                .data(saved)
                .build();
    }

    // -------------------------------------------------------------------------
    // Login
    // -------------------------------------------------------------------------

    /**
     * Authenticates a user and returns a JWT token.
     *
     * @param request the login payload
     * @return standardized response containing the JWT and user info
     */
    public BaseResponse<JwtAuthResponse> loginUser(JwtAuthRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            log.warn("[FACADE] loginUser — missing credentials");
            throw new InvalidDataException("Username and password are required");
        }

        JwtAuthResponse response = authService.authenticateUser(request);

        return BaseResponse.<JwtAuthResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Login successful")
                .data(response)
                .build();
    }

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    /**
     * Creates a new user (admin / internal path, no email duplication check).
     *
     * @param usersDTO the user payload
     * @return standardized response containing the created user DTO
     */
    public BaseResponse<UsersDTO> createUser(UsersDTO usersDTO) {
        requireUserPayload(usersDTO);

        UsersDTO created = userService.createUser(usersDTO);

        return BaseResponse.<UsersDTO>builder()
                .code(HttpStatus.CREATED.value())
                .message("User created successfully")
                .data(created)
                .build();
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    /**
     * Updates an existing user.
     *
     * @param usersDTO the updated fields
     * @param userId   the ID of the user to update
     * @return standardized response containing the updated user DTO
     */
    public BaseResponse<UsersDTO> updateUser(UsersDTO usersDTO, Integer userId) {
        if (userId == null || userId <= 0) {
            log.warn("[FACADE] updateUser — invalid userId: {}", userId);
            throw new InvalidDataException("User ID must be a positive integer");
        }
        if (usersDTO == null) {
            log.warn("[FACADE] updateUser — user payload is null for userId: {}", userId);
            throw new InvalidDataException("User data cannot be null");
        }
        UsersDTO updated = userService.updateUser(usersDTO, userId);

        return BaseResponse.<UsersDTO>builder()
                .code(HttpStatus.OK.value())
                .message("User updated successfully")
                .data(updated)
                .build();
    }

    // -------------------------------------------------------------------------
    // Get single user
    // -------------------------------------------------------------------------

    /**
     * Retrieves details of a single user by ID.
     *
     * @param userId the ID of the user to retrieve
     * @return standardized response containing the user DTO
     */
    public BaseResponse<UsersDTO> getUserDetail(Integer userId) {
        log.debug("[FACADE] getUserDetail — use-case started for userId: {}", userId);

        if (userId == null || userId <= 0) {
            log.warn("[FACADE] getUserDetail — invalid userId: {}", userId);
            throw new InvalidDataException("User ID must be a positive integer");
        }

        UsersDTO user = userService.getUserDetail(userId);

        log.debug("[FACADE] getUserDetail — use-case completed for userId: {}", userId);
        return BaseResponse.<UsersDTO>builder()
                .code(HttpStatus.OK.value())
                .message("User fetched successfully")
                .data(user)
                .build();
    }

    // -------------------------------------------------------------------------
    // Get all users
    // -------------------------------------------------------------------------

    /**
     * Retrieves all users.
     *
     * @return standardized response containing the list of user DTOs
     */
    public BaseResponse<List<UsersDTO>> getAllUsers() {
        log.debug("[FACADE] getAllUsers — use-case started");

        List<UsersDTO> users = userService.getAllUsers();

        log.debug("[FACADE] getAllUsers — use-case completed, returned {} user(s)", users.size());
        return BaseResponse.<List<UsersDTO>>builder()
                .code(HttpStatus.OK.value())
                .message("Users fetched successfully")
                .data(users)
                .build();
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    /**
     * Deletes a user by ID.
     *
     * @param userId the ID of the user to delete
     * @return standardized response confirming deletion
     */
    public BaseResponse<Void> deleteUser(Integer userId) {
        if (userId == null || userId <= 0) {
            log.warn("[FACADE] deleteUser — invalid userId: {}", userId);
            throw new InvalidDataException("User ID must be a positive integer");
        }
        userService.deleteUser(userId);

        return BaseResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("User deleted successfully")
                .build();
    }

    private void requireUserPayload(UsersDTO usersDTO) {
        if (usersDTO == null) {
            throw new InvalidDataException("User data cannot be null");
        }
    }

}

