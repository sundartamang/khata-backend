package com.khata.auth.service;

import com.khata.auth.repositories.UserRepo;
import com.khata.exceptions.ResourceNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public MyUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Loads the user details for the provided username (email).
     * Throws a ResourceNotFoundException if the user is not found in the repository.
     *
     * @param username The email address of the user to be fetched.
     * @return The UserDetails object containing user-specific information.
     * @throws UsernameNotFoundException If the user is not found by email.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepo.findByEmail(username).orElseThrow(
                ()-> new ResourceNotFoundException("User", "email", username));
    }
}
