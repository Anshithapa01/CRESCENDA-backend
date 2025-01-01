package com.crescenda.backend.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TaskSubmissionRequest {

	private String feedback;
    private String status;
    private String qaExpertUid;
    private long qaExpertId;
}
