package com.crescenda.backend.service;

import java.util.List;
import java.util.Optional;

import com.crescenda.backend.model.Task;
import com.crescenda.backend.request.TaskSubmissionRequest;
import com.crescenda.backend.response.QaRequestsResponse;
import com.crescenda.backend.response.TaskResponse;

public interface TaskService {

	void startTask(Long taskId, Long qaExpertUID);

	Optional<TaskResponse> findTasksByDraftId(Long draftId);

	void submitTask(Long draftId, TaskSubmissionRequest submissionRequest);

	List<QaRequestsResponse> getCompletedTasksWithDraftDetails();

	void updateDraftAndTask(Long taskId);

	Task getTaskById(Long taskId);

	void rejectDraft(Long taskId);

}
