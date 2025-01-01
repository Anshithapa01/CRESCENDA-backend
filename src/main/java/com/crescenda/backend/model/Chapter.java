package com.crescenda.backend.model;

import java.time.LocalDateTime;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chapter_id")
    private Long chapterId;

    @Column(name = "chapter_name")
    private String chapterName;

    @Column(name = "chapter_description")
    private String chapterDescription;

    @Column(name = "position")
    private Integer position;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    public Chapter(Long chapterId, String chapterName, String chapterDescription, Integer position,
                   LocalDateTime createdDate) {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.chapterDescription = chapterDescription;
        this.position = position;
        this.createdDate = createdDate;
    }

    // Relationships
    @ManyToOne
    @JoinColumn(name = "draft_id")
    @JsonBackReference("chapter-draft")  // Corresponds to the unique reference name in Draft
    private Draft draft;

    @OneToMany(mappedBy = "chapter")
    private List<MentorRequest> mentorRequests;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("chapter-material")
    private List<Material> materials;

  
}
