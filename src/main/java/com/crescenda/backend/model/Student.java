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
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Student {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "student_id")
	private long studentId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email_id")
	private String emailId;
	
	@Column(name = "phone_number")
	private String phoneNumber;

	private String password;
	
	@Column(name = "joined_date")
	private LocalDateTime joinedDate = LocalDateTime.now();
	
	private String link;
	
	private String role = "STUDENT";

	@Column(name = "is_blocked")
	private boolean isBlocked=false;
	
	@Column(name = "reset_password_token")
	private String resetPasswordToken;
    
    public Student(long studentId) {
        this.studentId = studentId;
    }
    
    public Student(String firstName, String lastName, String emailId, String phoneNumber, String password,boolean isBlocked) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.isBlocked=isBlocked;
    }
    
    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-wishlist")
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-rating")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-enrollment")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-attempts")
    private List<Attempt> studentAttempts;

    @OneToMany(mappedBy = "student")
    @JsonManagedReference("student-session")
    private List<Sessions> sessions;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MentorStudent> mentorStudents;
}
