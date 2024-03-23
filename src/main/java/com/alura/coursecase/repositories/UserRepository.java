package com.alura.coursecase.repositories;

import com.alura.coursecase.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Integer> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u.role FROM application_user u WHERE u.id = :id", nativeQuery = true)
    Optional<String> findRoleById(Integer id);

    Optional<UserModel> findByUsername(String username);

}
