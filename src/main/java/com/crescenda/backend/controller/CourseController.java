package com.crescenda.backend.controller;

import com.crescenda.backend.model.Course;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.Task;
import com.crescenda.backend.response.CourseResponse;
import com.crescenda.backend.service.CourseService;
import com.crescenda.backend.service.TaskService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses")
public class CourseController {

    private final CourseService courseService;
    private final TaskService taskService;

    public CourseController(CourseService courseService, TaskService taskService) {
        this.courseService = courseService;
        this.taskService = taskService;
    }

    @PostMapping("/create")
    public ResponseEntity<Course> createCourse(@RequestParam Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            if (task == null || task.getDraft() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null);
            }
            Draft draft = task.getDraft();
            Course newCourse = courseService.createCourse(draft);
            return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/draft/{draftId}/course-id")
    public ResponseEntity<Long> getCourseIdByDraftId(@PathVariable Long draftId) {
        try {
            Long courseId = courseService.getCourseIdByDraftId(draftId);
            if (courseId != null) {
                return ResponseEntity.ok(courseId);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{courseId}/block")
    public ResponseEntity<Course> blockCourse(@PathVariable Long courseId) {
        Course updatedCourse = courseService.blockCourse(courseId);
        return ResponseEntity.ok(updatedCourse);
    }

    
}
