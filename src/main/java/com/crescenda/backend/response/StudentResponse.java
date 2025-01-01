package com.crescenda.backend.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private long studentId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String link;
    @JsonProperty("isBlocked") 
    private boolean isBlocked;
    private LocalDateTime joinedDate;
    private String role;
    
    public StudentResponse(Long studentId){
    	this.studentId=studentId;
    }
}