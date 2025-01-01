package com.crescenda.backend.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Chapter;
import com.crescenda.backend.model.CourseQuality;
import com.crescenda.backend.model.Draft;
import com.crescenda.backend.model.MaterialQuality;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.model.Task;
import com.crescenda.backend.repository.CourseQualityRepository;
import com.crescenda.backend.repository.DraftRepository;
import com.crescenda.backend.repository.MaterialQualityRepository;
import com.crescenda.backend.repository.QaRepository;
import com.crescenda.backend.repository.TaskRepository;
import com.crescenda.backend.request.TaskSubmissionRequest;
import com.crescenda.backend.response.QaRequestsResponse;
import com.crescenda.backend.response.TaskResponse;
import com.crescenda.backend.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService{
	@Autowired
	TaskRepository taskRepository;
	@Autowired
	QaRepository qaRepository;
	@Autowired
	CourseQualityRepository courseQualityRepository;
	@Autowired
    private DraftRepository draftRepository;
	@Autowired
	MaterialQualityRepository materialQualityRepository;
	
	
	@Override
	public void startTask(Long taskId, Long qaExpertId) {
	    Task task = taskRepository.findById(taskId)
	            .orElseThrow(() -> new IllegalArgumentException("Task not found"));
	    if (task.getQa() != null && task.getQa().getQaId()!=qaExpertId) {
	        throw new IllegalStateException("This task is already assigned to a QA expert.");
	    }
	    QA qa = qaRepository.findById(qaExpertId)
	            .orElseThrow(() -> new IllegalArgumentException("QA Expert not found"));
	    task.setStatus("picked");
	    task.setQa(qa);
	    taskRepository.save(task);
	}
	
	@Override
	public void submitTask(Long draftId, TaskSubmissionRequest submissionRequest) {
		String qaExpertUid = submissionRequest.getQaExpertUid();
		long qaExpertId = submissionRequest.getQaExpertId();
		
		if (qaExpertUid == null || !qaExpertUid.startsWith("QL")) {
	        throw new RuntimeException("Invalid QA UID");
	    }
		QA qa = qaRepository.findById(qaExpertId)
	            .orElseThrow(() -> new RuntimeException("No QA found for qaExpertId: " + qaExpertId));

	    // Check if the qaUid matches the one passed in the request
	    if (!qa.getQaUid().equals(qaExpertUid)) {
	        throw new RuntimeException("QA UID mismatch: The qaExpertUid does not match the QA's qaUid.");
	    }
	    // Fetch the task by draftId
	    Task task = taskRepository.findByDraftDraftId(draftId)
	        .orElseThrow(() -> new RuntimeException("Task not found for draftId: " + draftId));

	    // Update task fields
	    task.setStatus("completed");
	    task.setQaExpertUID(qaExpertUid);

	    // Fetch the course quality record by draftId
	    CourseQuality courseQuality = courseQualityRepository.findByTaskTaskId(task.getTaskId())
	        .orElseThrow(() -> new RuntimeException("CourseQuality not found for draftId: " + draftId));

	    courseQuality.setFeedback(submissionRequest.getFeedback());
	    courseQuality.setStatus(submissionRequest.getStatus());
	    if (task.getQa() != null) {
	        Integer currentTaskCount = task.getQa().getTaskCount();
	        task.getQa().setTaskCount(currentTaskCount != null ? currentTaskCount + 1 : 1);
	    } else {
	        throw new RuntimeException("QA object is null for task with draftId: " + draftId);
	    }
	    // Save the updated entities
	    taskRepository.save(task);
	    courseQualityRepository.save(courseQuality);	    
	    qaRepository.save(qa);
	}

	
	@Override
	public Optional<TaskResponse> findTasksByDraftId(Long draftId) {
		return taskRepository.findByDraftDraftId(draftId)
	            .map(this::toTaskResponse);
    }
	
	@Override
	public List<QaRequestsResponse> getCompletedTasksWithDraftDetails() {
        // Fetch tasks with status "completed"
        List<Task> completedTasks = taskRepository.findByStatus("completed");

        // Map tasks to TaskResponse objects
        return completedTasks.stream()
        		.filter(task -> task.getDraft() != null && 
		                !"approved".equalsIgnoreCase(task.getDraft().getStatus()) &&
		                !"rejected".equalsIgnoreCase(task.getDraft().getStatus()))
                .map(task -> new QaRequestsResponse(
                        task.getTaskId(),
                        task.getDraft().getDraftId(),
                		task.getDraft().getThumbnailUrl(),
                        task.getDraft().getCourseName(),
                        task.getDraft().getCourseDescription(),
                        task.getDraft().getChapters().size(),
                        calculateMaterialCount(task.getDraft().getChapters()),
                        task.getDraft().getAddedDate(),
                        task.getCourseQuality().getStatus(),
                        task.getQa().getFirstName()
                ))
                .collect(Collectors.toList());
    }
	
	private TaskResponse toTaskResponse(Task task) {
	    TaskResponse taskResponse = new TaskResponse();
	    taskResponse.setTaskId(task.getTaskId());
	    taskResponse.setStatus(task.getStatus());
	    taskResponse.setCreatedAt(task.getCreatedAt());
	    taskResponse.setQaExpertUID(task.getQaExpertUID());
	    return taskResponse;
	}


	private int calculateMaterialCount(List<Chapter> chapters) {
        return chapters.stream()
                .mapToInt(chapter -> chapter.getMaterials() != null ? chapter.getMaterials().size() : 0)
                .sum();
    }
	
	@Override
	public void updateDraftAndTask(Long taskId) {
        // Fetch the task by taskId
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        CourseQuality courseQuality = courseQualityRepository.findByTaskTaskId(task.getTaskId())
    	        .orElseThrow(() -> new RuntimeException("CourseQuality not found"));
        
        Draft draft = task.getDraft();

        draft.setStatus("need improvement");
        draftRepository.save(draft);

        task.setStatus("pending");
        task.setQaExpertUID(null);
        task.setQa(null);
        List<MaterialQuality> materialQualities = materialQualityRepository.findByCourseQualityCourseQualityId(courseQuality.getCourseQualityId());

        // Set expertId to null for each MaterialQuality
        for (MaterialQuality materialQuality : materialQualities) {
            materialQuality.setExpertId(null);
        }

        // Save all updated MaterialQuality entities
        materialQualityRepository.saveAll(materialQualities);
        // Save the updated task
        taskRepository.save(task);
    }
	
	  	@Override
	    public void rejectDraft(Long taskId) {
		  
		  Task task = taskRepository.findById(taskId)
	                .orElseThrow(() -> new RuntimeException("Task not found"));
		  
	    	Draft draft= task.getDraft();
	    	draft.setStatus("rejected");
	    	draftRepository.save(draft);
	    }
	
	@Override
	public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElse(null);
    }
}
