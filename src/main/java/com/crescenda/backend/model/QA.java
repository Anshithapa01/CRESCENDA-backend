package com.crescenda.backend.model;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class QA {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qa_id")
    private Long qaId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String role;
    
    @Column(name = "email_id")
    private String emailId;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "qualification")
    private String qualification;
    
    @Column(name = "password")
    private String password;

    @Column(name = "experience")
    private Integer experience;

    @Column(name = "date_of_join")
    private LocalDate dateOfJoin=LocalDate.now();

    @Column(name = "task_count")
    private Integer taskCount;

    @Column(name = "areas_of_expertise")
    private String areasOfExpertise;

    @Column(name = "is_blocked")
    private Boolean isBlocked=false;
    
    @Column(name = "qa_uid")
    private String qaUid;

	public QA(Long qaId, String firstName, String lastName, String emailId, String phoneNumber, String role,
			String image, String qualification, String password, Integer experience, LocalDate dateOfJoin,
			Integer taskCount, String areasOfExpertise, Boolean isBlocked, String qaUid) {
		super();
		this.qaId = qaId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.role = role;
		this.qualification = qualification;
		this.password = password;
		this.experience = experience;
		this.dateOfJoin = dateOfJoin;
		this.taskCount = taskCount;
		this.areasOfExpertise = areasOfExpertise;
		this.isBlocked = isBlocked;
		this.qaUid = qaUid;
	}

	
	 	// Relationships 
	
		@OneToMany(mappedBy = "qa")
		@JsonManagedReference("qa-task")
	    private List<Task> taskAssignments;
		
		@ManyToOne
	    private QA lead; 
}
