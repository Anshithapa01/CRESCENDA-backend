package com.crescenda.backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseResponse {
    private Long courseId;
    private boolean isBlocked;
    private DraftResponse draft;
    
    public CourseResponse(Long courseId){
    	this.courseId=courseId;
    }
}