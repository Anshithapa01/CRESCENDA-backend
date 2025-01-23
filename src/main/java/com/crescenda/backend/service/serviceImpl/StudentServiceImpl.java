package com.crescenda.backend.service.serviceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Sessions;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.SessionRepository;
import com.crescenda.backend.repository.StudentRepository;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.response.StudentResponse;
import com.crescenda.backend.service.StudentService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;

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
	            new ResourceNotFoundException("Student not found with id: " + id));
	        student.setBlocked(isBlocked);
	        studentRepository.save(student);
	    }
		
		public void sendPasswordResetToken(String email) throws UserException {
	        Student student = studentRepository.findByEmailId(email);	        
	        if (student == null) {
	            throw new UserException("No account found with this email address.");
	        }
	        if (student.isBlocked()) {
	            throw new UserException("User is blocked. Please contact support.");
	        }
	        String token = UUID.randomUUID().toString();
	        student.setResetPasswordToken(token);
	        studentRepository.save(student);

	        // Send the reset link via email
	        String resetLink = "https://www.anshitha.cloud/reset-password?token=" + token;
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
	    public Student verifyGoogleAccessToken(String accessToken) throws UserException {
	        try {
	            // Use Google's modern libraries for ID token verification
	            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
	            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
	                    GoogleNetHttpTransport.newTrustedTransport(), jsonFactory)
	                    .setAudience(Collections.singletonList("866856697952-guoojs919a9ue3aqcl9qi1fmdqm1ka4b.apps.googleusercontent.com"))
	                    .build();

	            // Parse and verify the ID token
	            GoogleIdToken idToken = verifier.verify(accessToken);
	            if (idToken == null) {
	                throw new UserException("Invalid Google ID token");
	            }

	            // Extract user information
	            GoogleIdToken.Payload payload = idToken.getPayload();
	            String email = payload.getEmail();
	            boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

	            if (!emailVerified) {
	                throw new UserException("Google email is not verified");
	            }

	            // Fetch or create user in the database
	            Student student = studentRepository.findByEmailId(email);
	            if (student == null) {
	                throw new UserException("User not found. Please sign up first.");
	            }

	            return student;
	        } catch (Exception e) {
	            throw new UserException("Failed to verify Google access token: " + e.getMessage());
	        }
	    }

	    @Override
	    public AuthResponse signupWithGoogle(String idToken) throws Exception {
	        // Verify Google ID token
	        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
	                GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance())
	                .setAudience(Collections.singletonList("YOUR_CLIENT_ID")) // Replace with your Google Client ID
	                .build();

	        GoogleIdToken googleIdToken = verifier.verify(idToken);
	        if (googleIdToken == null) {
	            throw new UserException("Invalid Google ID token");
	        }

	        // Extract user information
	        GoogleIdToken.Payload payload = googleIdToken.getPayload();
	        String email = payload.getEmail();
	        boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());
	        String firstName = (String) payload.get("given_name");
	        String lastName = (String) payload.get("family_name");

	        if (!emailVerified) {
	            throw new UserException("Google email not verified");
	        }

	        // Check if the user already exists
	        if (studentRepository.findByEmailId(email) != null) {
	            throw new UserException("User already exists. Please log in.");
	        }

	        // Create and save the new student
	        Student newStudent = new Student();
	        newStudent.setEmailId(email);
	        newStudent.setFirstName(firstName);
	        newStudent.setLastName(lastName);
	        newStudent.setPassword(passwordEncoder.encode("DEFAULT_PASSWORD")); // Default password for OAuth users
	        studentRepository.save(newStudent);

	        return new AuthResponse(null, "Signup successful");
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
