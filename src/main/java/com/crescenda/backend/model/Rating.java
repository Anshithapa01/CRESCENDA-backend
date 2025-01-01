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
public class Rating {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rating_id")
    private Long ratingId;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "review_text")
    private String reviewText;

    @Column(name = "created_at")
    private LocalDate createdAt;
    
    @Column(name = "parent_id", nullable = true)
    private Long parentId;

    @Column(name = "root_id", nullable = true)
    private Long rootId;


	public Rating(Long ratingId, Integer rating, String reviewText, LocalDate createdAt) {
		super();
		this.ratingId = ratingId;
		this.rating = rating;
		this.reviewText = reviewText;
		this.createdAt = createdAt;
	}

	//Relationships

    @ManyToOne
    @JoinColumn(name = "course_id")
	@JsonBackReference("course-rating")
    private Course course;
    
	@ManyToOne
    @JoinColumn(name = "student_id")
	@JsonBackReference("student-rating")
    private Student student;

}
