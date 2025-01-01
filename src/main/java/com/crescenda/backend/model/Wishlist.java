package com.crescenda.backend.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Wishlist {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "wishlist_id")
    private Long wishlistId;

    @Column(name = "added_date")
    private LocalDate addedDate;

	public Wishlist(Long wishlistId, LocalDate addedDate) {
		super();
		this.wishlistId = wishlistId;
		this.addedDate = addedDate;
	}
    
	//Relationship
	
	@ManyToOne
    @JoinColumn(name = "student_id")
	@JsonBackReference("student-wishlist")
    private Student student;

	@ManyToOne
    @JoinColumn(name = "course_id")
	@JsonBackReference("course-wishlist")
    private Course course;
}
