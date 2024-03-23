package com.alura.coursecase.controllers.response;

import com.alura.coursecase.models.CourseModel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class GetCourseResponse {

    private Integer id;
    private GetCourseUserResponse instructor;
    private String name;
    private String code;
    private String description;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime inactivationDate;

    public static GetCourseResponse fromModel(CourseModel courseModel) {
        return GetCourseResponse.builder()
                .id(courseModel.getId())
                .instructor(GetCourseUserResponse.fromModel(courseModel.getInstructor()))
                .name(courseModel.getName())
                .code(courseModel.getCode())
                .description(courseModel.getDescription())
                .status(courseModel.getStatus())
                .creationDate(courseModel.getCreationDate())
                .inactivationDate(courseModel.getInactivationDate())
                .build();
    }
}
