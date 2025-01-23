package com.crescenda.backend.request;


import com.crescenda.backend.model.QA;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QARequest {
    
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String role;
    private String qualification;
    private Integer experience;
    private String areasOfExpertise;
    private String password;
    private QA lead; 

}