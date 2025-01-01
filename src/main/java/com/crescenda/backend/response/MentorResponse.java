package com.crescenda.backend.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MentorResponse {
	public MentorResponse(Long mentorId,
			String image,
			String firstName,
			String lastName,
			String emailId
			) {
		super();
		this.mentorId = mentorId;
		this.image=image;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
	}
	private Long mentorId;
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String headLine;
    private String image;
    private String areasOfExpertise;
    private String bio;
    private String highestQualification;
    private String websiteLink;
    private String linkedInLink;
    private String youtubeLink;
    private String facebookLink;
    private Boolean isBlocked;
    private LocalDateTime joinedDate;
    // Add fields for related entities as needed, but avoid full nesting.
    private List<DraftResponse> drafts;  // Include only required fields for drafts.
}