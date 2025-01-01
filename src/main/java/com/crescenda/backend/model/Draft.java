package com.crescenda.backend.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
@NoArgsConstructor
@Entity
public class Draft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "draft_id")
    private Long draftId;

    @Column(name = "course_name")
    private String courseName;

    @Column(name = "course_price")
    private BigDecimal coursePrice;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "course_description", length = 1000)
    private String courseDescription;

    @Column(name = "author_note",length = 500)
    private String authorNote;

    @Column(name = "special_note")
    private String specialNote;

    @Column(name = "course_prerequisite", length = 500)
    private String coursePrerequisite;  

    @Column(name = "level")
    private String level;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    private String language;
    
    private String type; //Beginner, Advanced, Intermediate

    @Column(name = "added_date")
    private LocalDate addedDate;

    @Column(name = "status")
    private String status = "draft";  //draft, pending, approved, rejected, need improvement

    
    
    // Relationships
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    @JsonBackReference("mentor-draft")  // Unique reference name for Mentor
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    @JsonBackReference("subcategory-draft")  // This should match the back reference in SubCategory
    private SubCategory subCategory; 

    @OneToOne(mappedBy = "draft")
    private Course course;

    @OneToMany(mappedBy = "draft",cascade = CascadeType.ALL, orphanRemoval = true )
    @JsonIgnore  // Ignore the quizzes in Draft to prevent recursion
    private List<Quiz> quizzes;

    @OneToMany(mappedBy = "draft", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("chapter-draft")  // Unique reference name for Chapter
    private List<Chapter> chapters;

    @OneToOne(mappedBy = "draft", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("draft-task")  // Unique name for the reference
    private Task task;
    
    @OneToMany(mappedBy = "draft")
    @JsonManagedReference("draft-studentAttempts")
    private List<Attempt> studentAttempts;

}
