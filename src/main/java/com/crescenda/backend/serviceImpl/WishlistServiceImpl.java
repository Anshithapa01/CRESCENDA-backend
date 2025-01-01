package com.crescenda.backend.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.model.SubCategory;
import com.crescenda.backend.model.Wishlist;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.repository.WishlistRepository;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.SubCategoryResponse;
import com.crescenda.backend.service.WishlistService;

@Service
public class WishlistServiceImpl implements WishlistService{
	
	@Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Override
    public void toggleWishlist(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        // Check if the student already has the course in the wishlist
        Wishlist existingWishlist = wishlistRepository.findByStudentAndCourse(student, course)
                .orElse(null);

        if (existingWishlist != null) {
            // If it exists, remove it from the wishlist
            wishlistRepository.delete(existingWishlist);
        } else {
            // If it does not exist, add it to the wishlist
            Wishlist wishlist = new Wishlist();
            wishlist.setCourse(course);
            wishlist.setStudent(student);
            wishlist.setAddedDate(LocalDate.now());
            wishlistRepository.save(wishlist);
        }
    }

    @Override
    public boolean isCourseInWishlist(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        Wishlist wishlist = wishlistRepository.findByStudentAndCourse(student, course).orElse(null);
        return wishlist != null;
    }

    @Override
    public List<CourseResponse> getWishlistByStudentId(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        List<Wishlist> wishlistItems = wishlistRepository.findByStudent(student);

        return wishlistItems.stream()
        		.filter(wishlist -> !wishlist.getCourse().isBlocked())
                .map(wishlist -> {
                    Course course = wishlist.getCourse();
                    CourseResponse courseResponse = new CourseResponse();
                    courseResponse.setCourseId(course.getCourseId());
                    courseResponse.setBlocked(course.isBlocked());
                    courseResponse.setDraft(convertToDraftResponse(course.getDraft()));
                    return courseResponse;
                })
                .collect(Collectors.toList());
    }

    public DraftResponse convertToDraftResponse(Draft draft) {
        DraftResponse draftResponse = new DraftResponse();
        draftResponse.setDraftId(draft.getDraftId());
        draftResponse.setCourseName(draft.getCourseName());
        draftResponse.setCoursePrice(draft.getCoursePrice());
        draftResponse.setSellingPrice(draft.getSellingPrice());
        draftResponse.setCourseDescription(draft.getCourseDescription());
        draftResponse.setAuthorNote(draft.getAuthorNote());
        draftResponse.setSpecialNote(draft.getSpecialNote());
        draftResponse.setCoursePrerequisite(draft.getCoursePrerequisite());
        draftResponse.setLevel(draft.getLevel());
        draftResponse.setLanguage(draft.getLanguage());
        draftResponse.setStatus(draft.getStatus());
        draftResponse.setType(draft.getType());
        draftResponse.setAddedDate(draft.getAddedDate());
        draftResponse.setThumbnailUrl(draft.getThumbnailUrl());
        draftResponse.setMentorName(draft.getMentor().getFirstName());
        if (draft.getSubCategory() != null) {
            draftResponse.setSubCategory(mapToSubCategoryResponse(draft.getSubCategory()));
        }
        if (draft.getMentor() != null) {
            draftResponse.setMentorName(draft.getMentor().getFirstName());
        }
        return draftResponse;
    }
    
    private SubCategoryResponse mapToSubCategoryResponse(SubCategory subCategory) {
        SubCategoryResponse response = new SubCategoryResponse();
        response.setSubcategoryName(subCategory.getSubcategoryName());
        response.setCatetoryName(subCategory.getCategory().getCategoryName());
        return response;
    }



}
