package com.crescenda.backend.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
public class Course {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private Long courseId;
	
	@Column(name = "is_blocked")
	private boolean isBlocked;

	public Course(Long courseId, boolean isBlocked) {
		super();
		this.courseId = courseId;
		this.isBlocked = isBlocked;
	}
	
	public Course(Long courseId) {
		super();
		this.courseId = courseId;
	}
	
	//Relationships
	
		@OneToOne
	    @JoinColumn(name = "draft_id")
	    private Draft draft;
		
		@OneToMany(mappedBy = "course")
	    private List<MentorRequest> mentorRequests;
		
		@OneToMany(mappedBy = "course")
		@JsonManagedReference("course-wishlist")
	    private List<Wishlist> wishlists;
		
		@OneToMany(mappedBy = "course")
		@JsonManagedReference("course-rating")
	    private List<Rating> ratings;
		
		@OneToMany(mappedBy = "course")
	    private List<Enrollment> enrollments;
		
		@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	    private List<MentorStudent> mentorStudents;

}
