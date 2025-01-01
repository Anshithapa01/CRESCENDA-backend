package com.crescenda.backend.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskResponse {
    private Long taskId;
    private String status;
    private LocalDate createdAt;
    private String qaExpertUID;
}