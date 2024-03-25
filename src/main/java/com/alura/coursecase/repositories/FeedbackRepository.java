package com.alura.coursecase.repositories;

import com.alura.coursecase.models.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<FeedbackModel, Integer> {
}
