package com.khata.auth.repositories;

import com.khata.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    // check if email is already taken
    Optional<User> findByEmail(String email);

    // Search users base on name (full name)
    Page<User> findByFullNameContainingIgnoreCase(String name, Pageable pageable);
}
