package com.crescenda.backend.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService{
	
	private final CourseRepository courseRepository;
    private final DraftRepository draftRepository;

    public CourseServiceImpl(CourseRepository courseRepository, DraftRepository draftRepository) {
        this.courseRepository = courseRepository;
        this.draftRepository = draftRepository;
    }

    @Override
    public Course createCourse(Draft draft) {
        draft.setStatus("approved");
        draftRepository.save(draft);

        Course course = new Course();
        course.setDraft(draft); 
        course.setBlocked(false);
        return courseRepository.save(course);
    }
    
    @Override
    public List<CourseResponse> getAllCourses() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream()
        		.filter(course -> !course.isBlocked())
                .map(course -> {
                    CourseResponse response = new CourseResponse();
                    response.setCourseId(course.getCourseId());
                    response.setBlocked(course.isBlocked());

                    // Map Draft to DraftResponse
                    if (course.getDraft() != null) {
                        DraftResponse draftResponse = new DraftResponse();
                        Draft draft = course.getDraft();
                        draftResponse.setDraftId(draft.getDraftId());
                        draftResponse.setCourseName(draft.getCourseName());
                        draftResponse.setCourseDescription(draft.getCourseDescription());
                        draftResponse.setAuthorNote(draft.getAuthorNote());
                        draftResponse.setSpecialNote(draft.getSpecialNote());
                        draftResponse.setCoursePrerequisite(draft.getCoursePrerequisite());
                        draftResponse.setLevel(draft.getLevel());
                        draftResponse.setLanguage(draft.getLanguage());
                        draftResponse.setType(draft.getType());
                        draftResponse.setStatus(draft.getStatus());
                        draftResponse.setThumbnailUrl(draft.getThumbnailUrl());
                        draftResponse.setCoursePrice(draft.getCoursePrice());
                        draftResponse.setSellingPrice(draft.getSellingPrice());
                        draftResponse.setAddedDate(draft.getAddedDate());
                        draftResponse.setChaptersCount(draft.getChapters().size()); 
                        int totalMaterials = draft.getChapters().stream()
                                .mapToInt(chapter -> chapter.getMaterials().size())
                                .sum();
                        draftResponse.setMaterialsCount(totalMaterials);

                        // Include SubCategory and Mentor details if available
                        if (draft.getSubCategory() != null) {
                            draftResponse.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
                        }
                        if (draft.getMentor() != null) {
                            draftResponse.setMentorName(draft.getMentor().getFirstName());
                        }

                        response.setDraft(draftResponse);
                    }
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public Long getCourseIdByDraftId(Long draftId) {
        return courseRepository.findByDraft_DraftId(draftId)
                               .map(Course::getCourseId)
                               .orElse(null);
    }
    
    @Override
    public Course blockCourse(Long courseId) {
    	Course course = courseRepository.findByDraft_DraftId(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found for Draft ID: " + courseId));
        course.setBlocked(true);
        return courseRepository.save(course);
    }

    public SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory) {
        SubCategoryResponse response = new SubCategoryResponse();
        response.setSubcategoryId(subCategory.getSubcategoryId());
        response.setSubcategoryName(subCategory.getSubcategoryName());
        response.setSubCategoryDescription(subCategory.getSubCategoryDescription());
        response.setCatetoryName(subCategory.getCategory().getCategoryName());
        response.setCategoryId(subCategory.getCategory().getCategoryId());
        response.setIsDeleted(subCategory.getIsDeleted());
        return response;
    }

}
