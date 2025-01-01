package com.crescenda.backend.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.model.Sessions;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.SessionRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.response.QAResponse;
import com.crescenda.backend.response.StudentResponse;
import com.crescenda.backend.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{

	private final StudentRepository studentRepository;
	private PasswordEncoder passwordEncoder;
	private JwtProvider jwtProvider;
	private CustomUserServiceImplementation customUserServiceImplementation;
    private JavaMailSender mailSender;
	private SessionRepository sessionRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
    		PasswordEncoder passwordEncoder,
    		JwtProvider jwtProvider,
    		CustomUserServiceImplementation customUserServiceImplementation,
    		JavaMailSender mailSender,
    		SessionRepository sessionRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtProvider=jwtProvider;
        this.customUserServiceImplementation=customUserServiceImplementation;
        this.mailSender=mailSender;
        this.sessionRepository=sessionRepository;
    }
    
	@Override
	public Student findStudentById(long student_id) throws UserException {
		Optional<Student>student=studentRepository.findById(student_id);
		if(student.isPresent()) {
			return student.get();
		}
		throw new UserException("Student not found with id "+student_id);
	}

	@Override
	public StudentResponse findUserProfileResponseByJwt(String jwt) throws UserException {
	    String email = jwtProvider.getEmailFromToken(jwt);
	    Student student = studentRepository.findByEmailId(email);

	    if (student == null) {
	        throw new UserException("Student not found with email " + email);
	    }

	    // Map Student to StudentResponse
	    return mapToStudentResponse(student);
	}


	@Override
	public Student registerStudent(Student student) throws UserException {
		String email = student.getEmailId();
        Student existingStudent = studentRepository.findByEmailId(email);

        if (existingStudent != null) {
            throw new UserException("Email is already used with another account");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        return studentRepository.save(student);
	}
	
	@Override
	public Authentication authenticate(String username, String password) {
		Student student = studentRepository.findByEmailId(username);
		if (student == null) {
	        throw new BadCredentialsException("Invalid Username");
	    }

	    // Check if the student is blocked
	    if (student.isBlocked()) {
	        throw new BadCredentialsException("User is blocked. Please contact support.");
	    }
	    
	    List<Sessions> activeSessions = sessionRepository.findByStudent(student);
        for (Sessions session : activeSessions) {
            sessionRepository.delete(session);
        }        

	    // Validate the password
	    if (!passwordEncoder.matches(password, student.getPassword())) {
	        throw new BadCredentialsException("Invalid Password");
	    }
		return new UsernamePasswordAuthenticationToken(
		        new User(student.getEmailId(), student.getPassword(), new ArrayList<>()),
		        null,
		        new ArrayList<>()
		    );
	}
	
	@Override
	public void createSession(String username,String token){
		Student student = studentRepository.findByEmailId(username);
        Sessions newSession = new Sessions();
        newSession.setCreatedAt(LocalDateTime.now());
        newSession.setUpdatedAt(LocalDateTime.now());
        newSession.setSessionToken(token);
        newSession.setStudent(student);
        sessionRepository.save(newSession); 
	}
	
	@Override
	 public StudentResponse getStudentById(long id) {
	        Optional<Student> studentOptional = studentRepository.findById(id);
	        if (studentOptional.isPresent()) {
	            Student student = studentOptional.get();
	            return mapToStudentResponse(student);
	        }
	        return null;
	    }
	
	@Override
	public List<StudentResponse> getAllStudents() {
        return studentRepository.findAll().stream()
        		.map(this::mapToStudentResponse).collect(Collectors.toList());
    }

		@Override
	    public StudentResponse updateStudentById(long id, Student updatedStudent) {
	        Optional<Student> studentOptional = studentRepository.findById(id);
	        if (studentOptional.isPresent()) {
	            Student existingStudent = studentOptional.get();
	            existingStudent.setFirstName(updatedStudent.getFirstName());
	            existingStudent.setLastName(updatedStudent.getLastName());
	            existingStudent.setBlocked(updatedStudent.isBlocked());
	            existingStudent.setEmailId(updatedStudent.getEmailId());
	            existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
	            existingStudent.setLink(updatedStudent.getLink());
	            studentRepository.save(existingStudent);
	            return mapToStudentResponse(existingStudent);
	        }
	        return null;
	    }
		
		@Override
		public void setBlockedStatus(Long id, boolean isBlocked) {
	        Student student = studentRepository.findById(id).orElseThrow(() -> 
	            new ResourceNotFoundException("QA not found with id: " + id));
	        student.setBlocked(isBlocked);
	        studentRepository.save(student);
	    }
		
		public void sendPasswordResetToken(String email) throws UserException {
	        Student student = studentRepository.findByEmailId(email);	        
	        if (student == null) {
	            throw new UserException("No account found with this email address.");
	        }
	        String token = UUID.randomUUID().toString();
	        student.setResetPasswordToken(token);
	        studentRepository.save(student);

	        // Send the reset link via email
	        String resetLink = "http://localhost:5173/reset-password?token=" + token;
	        sendEmail(email, "Password Reset Request", 
	            "Click the link to reset your password: " + resetLink);
	    }

	    private void sendEmail(String to, String subject, String body) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setTo(to);
	        message.setSubject(subject);
	        message.setText(body);
	        mailSender.send(message);
	    }
	    
	    @Override
	    public void resetPassword(String token, String newPassword) throws UserException {
	        Student student = studentRepository.findByResetPasswordToken(token);

	        if (student == null) {
	            throw new UserException("Invalid or expired reset token.");
	        }
	        
	        if (passwordEncoder.matches(newPassword, student.getPassword())) {
	            throw new UserException("New password cannot be the same as the previous password.");
	        }
	        
	        student.setPassword(passwordEncoder.encode(newPassword)); 
	        student.setResetPasswordToken(null); // Clear the token
	        studentRepository.save(student);
	    }


		@Override
		public StudentResponse mapToStudentResponse(Student student) {
	        return new StudentResponse(
	                student.getStudentId(),
	                student.getFirstName(),
	                student.getLastName(),
	                student.getEmailId(),
	                student.getPhoneNumber(),
	                student.getLink(),
	                student.isBlocked(),
	                student.getJoinedDate(),
	                student.getRole()
	        );
	    }

}
