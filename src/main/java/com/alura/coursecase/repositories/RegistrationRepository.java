package com.alura.coursecase.repositories;

import com.alura.coursecase.models.RegistrationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<RegistrationModel, Integer> {

    @Query(value = "SELECT IF(COUNT(r.id) > 0, 'true', 'false') FROM registration r where r.id_user =?1 AND r.id_course = ?2", nativeQuery = true)
    boolean existsByIdUserAndIdCourse(Integer idUser, Integer idCourse);

    @Query(value = "SELECT * FROM registration r WHERE r.id_course IN ( SELECT id_course FROM registration GROUP BY id_course HAVING COUNT(id_course) >= 4)", nativeQuery = true)
    Optional<List<RegistrationModel>> findByIdCourseGreaterThanThreeOcurrences();
}
