package com.crescenda.backend.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.model.Task;
import com.crescenda.backend.request.QAExpertRequest;
import com.crescenda.backend.request.TaskSubmissionRequest;
import com.crescenda.backend.response.QaRequestsResponse;
import com.crescenda.backend.response.TaskResponse;
import com.crescenda.backend.service.TaskService;

@RestController
@RequestMapping("/api/qa/tasks")
public class TaskController {
	
	@Autowired
	TaskService taskService; 
	
	@GetMapping("/draft/{draftId}")
	public ResponseEntity<Optional<TaskResponse>> getTasksByDraftId(@PathVariable Long draftId) {
	    Optional<TaskResponse> taskResponse = taskService.findTasksByDraftId(draftId);
	    return ResponseEntity.ok(taskResponse);
	}
	
	@PostMapping("/{taskId}/start")
	public ResponseEntity<String> startTask(
	        @PathVariable Long taskId, 
	        @RequestBody QAExpertRequest request) {
		try {
		    taskService.startTask(taskId, request.getQaExpertUID());
		    return ResponseEntity.ok("Task status changed to 'picked' and QA Expert assigned");
		} catch (IllegalArgumentException | ResourceNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving review: " + e.getMessage());
	    }
	}

	@PostMapping("/submit/{draftId}")
	public ResponseEntity<String> submitTask(
	        @PathVariable Long draftId, 
	        @RequestBody TaskSubmissionRequest submissionRequest) {
		try {
		    taskService.submitTask(draftId, submissionRequest);
		    return ResponseEntity.ok("Task submitted successfully!");
		} catch (IllegalArgumentException | ResourceNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving review: " + e.getMessage());
	    }
	}
	
	@GetMapping("/completed")
    public ResponseEntity<List<QaRequestsResponse>> getCompletedTasksWithDraftDetails() {
        List<QaRequestsResponse> completedTasks = taskService.getCompletedTasksWithDraftDetails();
        return ResponseEntity.ok(completedTasks);
    }
	
	@PutMapping("/reject/{taskId}")
    public ResponseEntity<String> rejectDraft(@PathVariable Long taskId){
    	taskService.rejectDraft(taskId);
		return ResponseEntity.ok("Task rejected'"); 
    }
	
	@PutMapping("/update/{taskId}")
    public ResponseEntity<String> updateDraftAndTask(@PathVariable Long taskId) {
        try {
            taskService.updateDraftAndTask(taskId);
            return ResponseEntity.ok("Draft and Task updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
    }

}
