package com.alura.coursecase.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "course")
public class CourseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="id_instructor")
    private UserModel instructor;
    @Column
    private String name;
    @Column
    private String code;
    @Column
    private String description;
    @Column
    @Setter
    private String status;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "inactivation_date")
    @Setter
    private LocalDateTime inactivationDate;

    public CourseModel(Integer id) {
        this.id = id;
    }
}
