package com.crescenda.backend.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Chat;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorStudent;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.ChatRepository;
import com.crescenda.backend.repository.MentorRepository;
import com.crescenda.backend.repository.MentorStudentRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.response.ChatResponse;
import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.response.MentorStudentResponse;
import com.crescenda.backend.service.ChatService;

@Service
public class ChatServiceImpl implements ChatService{
	
	private final ChatRepository chatRepository;
    private final MentorStudentRepository mentorStudentRepository;
    private final StudentRepository studentRepository;
    private final MentorRepository mentorRepository;
    
    

    public ChatServiceImpl(ChatRepository chatRepository, MentorStudentRepository mentorStudentRepository,
			StudentRepository studentRepository,
			MentorRepository mentorRepository) {
		super();
		this.chatRepository = chatRepository;
		this.mentorStudentRepository = mentorStudentRepository;
		this.studentRepository = studentRepository;
		this.mentorRepository=mentorRepository;
	}

	@Override
    public ChatResponse createChat(long reqStudent, long mentorId) throws UserException {
        Student Student1 = studentRepository.findById(reqStudent).orElseThrow(() -> new UserException("Student not found with ID: " + reqStudent));
        Mentor mentor = mentorRepository.findById(mentorId).orElseThrow(() -> new UserException("Mentor not found with ID: " + mentorId));

        MentorStudent mentorStudent = mentorStudentRepository
                .findByStudentAndMentor(Student1, mentor)
                .orElseThrow(() -> new UserException("No mentor-student relationship found."));

        Chat chat = chatRepository.findByMentorStudent(mentorStudent).orElse(null);
        if (chat != null) {
            // If the chat already exists, ensure both sides are visible
            chat.setMentorVisible(true);
            chat.setStudentVisible(true);
            chatRepository.save(chat);
            return toChatResponse(chat);
        }

        // Create a new chat
        Chat newChat = new Chat();
        newChat.setMentorStudent(mentorStudent);
        newChat.setLastUpdated(LocalDateTime.now());
        newChat.setMentorVisible(true);
        newChat.setStudentVisible(true);
        chatRepository.save(newChat);

        return toChatResponse(newChat);
    }

    @Override
    public ChatResponse deleteChat(long chatId, long reqStudent) throws UserException {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new UserException("Chat not found with ID: " + chatId));

        if (chat.getMentorStudent().getStudent().getStudentId() == reqStudent) {
            chat.setStudentVisible(false);
        } else if (chat.getMentorStudent().getMentor().getMentorId() == reqStudent) {
            chat.setMentorVisible(false);
        } else {
            throw new UserException("You do not have permission to delete this chat.");
        }

        // If both sides are hidden, delete the chat
        if (!chat.isMentorVisible() && !chat.isStudentVisible()) {
            chatRepository.delete(chat);
        } else {
            chatRepository.save(chat);
        }

        return toChatResponse(chat);
    }

    @Override
    public List<ChatResponse> findAllchatByStudendtId(long studentId) {
        return chatRepository
                .findAllByMentorStudentStudentStudentIdAndStudentVisibleTrue(studentId)
                .stream()
                .map(this::toChatResponse)
                .toList();
    }

    @Override
    public List<ChatResponse> findAllchatByMentorId(long mentorId) {
        return chatRepository
                .findAllByMentorStudentMentorMentorIdAndMentorVisibleTrue(mentorId)
                .stream()
                .map(this::toChatResponse)
                .toList();
    }

    @Override
    public ChatResponse findChatByStudentId(long studentId) {
        Chat chat = chatRepository.findFirstByMentorStudentStudentStudentIdAndStudentVisibleTrue(studentId);
        return chat != null ? toChatResponse(chat) : null;
    }

    @Override
    public ChatResponse findChatByMentorId(long mentorId) {
        Chat chat = chatRepository.findFirstByMentorStudentMentorMentorIdAndMentorVisibleTrue(mentorId);
        return chat != null ? toChatResponse(chat) : null;
    }

    // Helper method to convert Chat entity to ChatResponse DTO
    private ChatResponse toChatResponse(Chat chat) {
        return new ChatResponse(
                chat.getId(),
                chat.getLastMessage(),
                chat.getLastUpdated(),
                new MentorStudentResponse(
                        chat.getMentorStudent().getId(),
                        chat.getMentorStudent().getMentor().getMentorId(),
                        chat.getMentorStudent().getMentor().getFirstName(),
                        chat.getMentorStudent().getMentor().getLastName(),
                        chat.getMentorStudent().getMentor().getEmailId(),
                        chat.getMentorStudent().getMentor().getImage(),
                        chat.getMentorStudent().getStudent().getStudentId(),
    	                chat.getMentorStudent().getStudent().getFirstName(),
                		chat.getMentorStudent().getStudent().getLastName(),
                		chat.getMentorStudent().getStudent().getEmailId(),
                        chat.getMentorStudent().getCourse() != null ? chat.getMentorStudent().getCourse().getCourseId() : null
                )
        );
    }
}
