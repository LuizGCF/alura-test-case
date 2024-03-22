package com.alura.coursecase.repositories;

import com.alura.coursecase.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<UserModel> findByUsername(String username);

}
