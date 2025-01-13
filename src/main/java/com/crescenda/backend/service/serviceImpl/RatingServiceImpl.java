package com.crescenda.backend.service.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Rating;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.CourseRepository;
import com.crescenda.backend.repository.RatingRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.response.RatingResponse;
import com.crescenda.backend.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService{
	
	@Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Rating createRating(Long courseId, Long studentId, Integer ratingValue, String reviewText) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Course or Student not found.");
        }

        Rating rating = new Rating();
        rating.setRating(ratingValue);
        rating.setReviewText(reviewText);
        rating.setCreatedAt(LocalDate.now());
        rating.setCourse(courseOpt.get());
        rating.setStudent(studentOpt.get());

        return ratingRepository.save(rating);
    }

    @Override
    public List<RatingResponse> getRatingsByCourseId(Long courseId) {
        return ratingRepository.findByCourse_CourseId(courseId)
                .stream()
                .map(rating -> {
                    RatingResponse response = new RatingResponse();
                    response.setRatingId(rating.getRatingId());
                    response.setRating(rating.getRating());
                    response.setReviewText(rating.getReviewText());
                    response.setCreatedAt(rating.getCreatedAt());
                    response.setStudentId(rating.getStudent().getStudentId());
                    response.setStudentName(rating.getStudent().getFirstName());
                    response.setParentId(rating.getParentId());
                    response.setRootId(rating.getRootId());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean hasStudentRatedCourse(Long courseId, Long studentId) {
        return ratingRepository.existsByCourseCourseIdAndStudentStudentId(courseId, studentId);
    }
    
    @Override
    public List<RatingResponse> getCommentsByRootId(Long rootId) {
        return ratingRepository.findByRootId(rootId)
                .stream()
                .map(comment -> {
                    RatingResponse response = new RatingResponse();
                    response.setRatingId(comment.getRatingId());
                    response.setReviewText(comment.getReviewText());
                    response.setCreatedAt(comment.getCreatedAt());
                    response.setStudentId(comment.getStudent().getStudentId());
                    response.setStudentName(comment.getStudent().getFirstName());
                    System.out.println(comment.getParentId());
                    // Set parent name if parentId exists
                    if (comment.getParentId() != null) {
                        ratingRepository.findById(comment.getParentId()).stream()
                                .findFirst()
                                .ifPresent(parent ->
                                        response.setParentName(parent.getStudent().getFirstName()));
                                
                    } else {
                        response.setParentName(""); // No parent, set as empty
                    }
                    
                    response.setParentId(comment.getParentId());
                    response.setRootId(comment.getRootId());
                    return response;
                })
                .collect(Collectors.toList());
    }


    @Override
    public Rating addComment(Long courseId, Long studentId, Long parentId, Long rootId, String reviewText) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (courseOpt.isEmpty() || studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Course or Student not found.");
        }

        Rating comment = new Rating();
        comment.setReviewText(reviewText);
        comment.setCreatedAt(LocalDate.now());
        comment.setCourse(courseOpt.get());
        comment.setStudent(studentOpt.get());
        comment.setParentId(parentId);
        comment.setRootId(rootId != null ? rootId : parentId); // Set rootId

        return ratingRepository.save(comment);
    }


}
