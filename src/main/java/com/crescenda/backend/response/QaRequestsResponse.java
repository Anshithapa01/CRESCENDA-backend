package com.crescenda.backend.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QaRequestsResponse {

	private Long taskId;
	private Long draftId;
    private String thumbnailUrl;
    private String courseName;
    private String courseDescription;
    private Integer chapterCount;
    private Integer materialCount; // Total materials in all chapters
    private LocalDate addedDate;
    private String status;
    private String assignedTo;
}
