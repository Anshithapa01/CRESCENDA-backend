package com.crescenda.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentorStudentResponse {
    private Long id;
    private Long mentorId;
    private String mentorFirstName;
    private String mentorLastName;
    private String mentorUsername;
    private String mentorImage;
    private Long studentId;
    private String studentFirstName;
    private String studentLastName;
    private String studentUsername;
    private Long courseId; 
}