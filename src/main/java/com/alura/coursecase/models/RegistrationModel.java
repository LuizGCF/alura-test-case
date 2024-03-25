package com.alura.coursecase.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "registration")
public class RegistrationModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="id_user")
    private UserModel user;
    @ManyToOne
    @JoinColumn(name="id_course")
    private CourseModel course;
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    @OneToOne
    @JoinColumn(name = "id_feedback")
    @Setter
    private FeedbackModel feedback;
}
