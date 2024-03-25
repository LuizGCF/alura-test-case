package com.alura.coursecase.controllers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GetCoursesReportResponse {
    private Integer idCourse;
    private String courseName;
    private Long totalRegistrations;
    private Integer totalFeedbacks;
    private Float nps;
    private String status;
}
