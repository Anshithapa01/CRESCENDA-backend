package com.crescenda.backend.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Mentor {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mentor_id")
    private Long mentorId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id")
    private String emailId;

    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(name = "password")
    private String password;

    @Column(name = "head_line")
    private String headLine;

    @Column(name = "image")
    private String image;

    @Column(name = "areas_of_expertise", length = 500)
    private String areasOfExpertise;

    @Column(name = "bio", length = 500)
    private String bio;

    @Column(name = "highest_qualification")
    private String highestQualification;

    @Column(name = "website_link")
    private String websiteLink;

    @Column(name = "linked_in_link")
    private String linkedInLink;

    @Column(name = "youtube_link")
    private String youtubeLink;

    @Column(name = "facebook_link")
    private String facebookLink;

    @Column(name = "is_blocked")
    private Boolean isBlocked=false;

    @Column(name = "joined_date")
    private LocalDateTime joinedDate=LocalDateTime.now();

    @Column(name = "reset_password_token")
    private String resetPasswordToken;

	public Mentor(Long mentorId, String firstName, String lastName, String emailId, String phoneNumber, String password,
			String headLine, String image, String areasOfExpertise, String bio, String highestQualification,
			String websiteLink, String linkedInLink, String youtubeLink, String facebookLink, Boolean isBlocked,
			LocalDateTime joinedDate, String resetPasswordToken) {
		super();
		this.mentorId = mentorId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.headLine = headLine;
		this.image = image;
		this.areasOfExpertise = areasOfExpertise;
		this.bio = bio;
		this.highestQualification = highestQualification;
		this.websiteLink = websiteLink;
		this.linkedInLink = linkedInLink;
		this.youtubeLink = youtubeLink;
		this.facebookLink = facebookLink;
		this.isBlocked = isBlocked;
		this.joinedDate = joinedDate;
		this.resetPasswordToken = resetPasswordToken;
	}

    
	// Relationship 
	
    @OneToMany(mappedBy = "mentor")
    private List<MentorPayment> mentorPayments;
	
	@OneToMany(mappedBy = "mentor")
	@JsonManagedReference("mentor-draft")
    private List<Draft> drafts;
	
	@OneToMany(mappedBy = "mentor")
    private List<MentorRequest> mentorRequests;
	
	@OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorStudent> mentorStudents;
}
