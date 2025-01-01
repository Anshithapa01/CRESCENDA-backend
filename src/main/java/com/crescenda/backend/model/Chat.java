package com.crescenda.backend.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat")
public class Chat {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_message", nullable = true)
    private String lastMessage; // Optionally store the latest message for easier querying.

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated; // Timestamp for the last activity in the chat.

    @OneToOne
    @JoinColumn(name = "mentor_student_id", nullable = false, unique = true)
    private MentorStudent mentorStudent;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>(); 
    
    @Column(name = "mentor_visible", nullable = false)
    private boolean mentorVisible = true;

    @Column(name = "student_visible", nullable = false)
    private boolean studentVisible = true;
}
