package com.crescenda.backend.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Chat;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorStudent;
import com.crescenda.backend.repository.ChatRepository;
import com.crescenda.backend.repository.MentorStudentRepository;
import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.response.MentorStudentResponse;
import com.crescenda.backend.service.MentorStudentService;

@Service
public class MentorStudentServiceImpl implements MentorStudentService{
	
	@Autowired
    private MentorStudentRepository mentorStudentRepository;
	@Autowired
	private ChatRepository chatRepository;
	
	@Override
	public List<MentorStudentResponse> getAllMentorsByStudentId(Long studentId) {
	    // Fetch MentorStudent entities associated with the student
	    List<MentorStudent> mentorStudents = mentorStudentRepository.findAllByStudentId(studentId);
	    
	    return mentorStudents.stream()
	            .map(mentorStudent -> {
	            	Optional<Chat> chat = chatRepository.findByMentorStudent(mentorStudent);
	                MentorStudentResponse response = new MentorStudentResponse();
	                response.setId(chat.map(Chat::getId).orElse(null)); // Set mentorStudent ID
	                response.setMentorId(mentorStudent.getMentor().getMentorId());
	                response.setMentorFirstName(mentorStudent.getMentor().getFirstName());
	                response.setMentorLastName(mentorStudent.getMentor().getLastName());
	                response.setMentorImage(mentorStudent.getMentor().getImage());
	                response.setStudentId(studentId);
	                response.setStudentFirstName(mentorStudent.getStudent().getFirstName());
	                response.setStudentLastName(mentorStudent.getStudent().getLastName());
	                response.setCourseId(mentorStudent.getCourse() != null ? mentorStudent.getCourse().getCourseId() : null);
	                return response;
	            })
	            .collect(Collectors.toList());
	}


	@Override
	public List<MentorStudentResponse> searchMentorsByStudentIdAndQuery(Long studentId, String query) {
	    List<MentorStudent> mentorStudents = mentorStudentRepository.searchByStudentIdAndQuery(studentId, query.toLowerCase());
	    
	    return mentorStudents.stream()
	            .map(mentorStudent -> {
	            	Optional<Chat> chat = chatRepository.findByMentorStudent(mentorStudent);
	                MentorStudentResponse response = new MentorStudentResponse();
	                response.setId(chat.map(Chat::getId).orElse(null));  // Set mentorStudent ID
	                response.setMentorId(mentorStudent.getMentor().getMentorId());
	                response.setMentorFirstName(mentorStudent.getMentor().getFirstName());
	                response.setMentorLastName(mentorStudent.getMentor().getLastName());
	                response.setMentorImage(mentorStudent.getMentor().getImage());
	                response.setStudentId(studentId);
	                response.setStudentFirstName(mentorStudent.getStudent().getFirstName());
	                response.setStudentLastName(mentorStudent.getStudent().getLastName());
	                response.setCourseId(mentorStudent.getCourse() != null ? mentorStudent.getCourse().getCourseId() : null);
	                return response;
	            })
	            .collect(Collectors.toList());
	}



	
}
