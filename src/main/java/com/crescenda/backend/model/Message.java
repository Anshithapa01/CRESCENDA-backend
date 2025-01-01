package com.crescenda.backend.model;

import java.time.LocalDateTime;

import com.crescenda.backend.enumerator.SenderRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Message {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "content", nullable = false)
	    private String content;

	    @Column(name = "timestamp", nullable = false)
	    private LocalDateTime timestamp;

	    @Column(name = "sender_role", nullable = false)
	    @Enumerated(EnumType.STRING)
	    private SenderRole senderRole; // Enum: STUDENT or MENTOR

	    @ManyToOne
	    @JoinColumn(name = "chat_id", nullable = false)
	    private Chat chat;
	    
	    @Column(name = "file_url", nullable = true)
	    private String fileUrl;

}
