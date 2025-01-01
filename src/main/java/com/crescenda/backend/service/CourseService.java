package com.crescenda.backend.service;

import java.util.List;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.response.CourseResponse;

public interface CourseService {

	Course createCourse(Draft draft);

	List<CourseResponse> getAllCourses();

	Long getCourseIdByDraftId(Long draftId);

	Course blockCourse(Long courseId);

}
