package com.crescenda.backend.response;

import java.time.LocalDate;

import com.crescenda.backend.model.QA;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QAResponse {
    private Long qaId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String role;
    private String qualification;
    private Integer experience;
    private LocalDate dateOfJoin;
    private Integer taskCount;
    private String areasOfExpertise;
    private String password;
    private String qaUid;
    private QA lead;
    private Boolean isBlocked;
}
