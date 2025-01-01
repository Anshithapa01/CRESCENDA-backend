package com.crescenda.backend.model;

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
public class Quiz {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "quiz_id")
    private Long quizId;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "option_1")
    private String option1;

    @Column(name = "option_2")
    private String option2;

    @Column(name = "option_3")
    private String option3;

    @Column(name = "option_4")
    private String option4;
   
	@ManyToOne
    @JoinColumn(name = "draft_id")
    private Draft draft;
}
