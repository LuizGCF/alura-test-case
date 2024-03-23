package com.alura.coursecase.repositories;

import com.alura.coursecase.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseModel, Integer> {
    boolean existsByCode(String code);
    Optional<CourseModel> findByCode(String code);

    @Query(value = "SELECT * FROM course c WHERE c.status=?1", nativeQuery = true)
    Page<CourseModel> findByStatus (String status, Pageable pageable);


}
