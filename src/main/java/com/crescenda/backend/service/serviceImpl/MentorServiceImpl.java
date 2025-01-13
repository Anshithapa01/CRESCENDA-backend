package com.crescenda.backend.service.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.model.MentorPayment;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.MentorPaymentRepository;
import com.crescenda.backend.repository.MentorRepository;
import com.crescenda.backend.response.DraftResponse;
import com.crescenda.backend.response.MentorResponse;
import com.crescenda.backend.service.MentorService;

@Service
public class MentorServiceImpl implements MentorService{

	private final MentorRepository mentorRepository;
	private PasswordEncoder passwordEncoder;
	private JwtProvider jwtProvider;
	private CustomMentorServiceImpl customMentorServiceImpl;
	private MentorPaymentRepository mentorPaymentRepository;
	private JavaMailSender mailSender;

    public MentorServiceImpl(MentorRepository mentorRepository,
    		PasswordEncoder passwordEncoder,
    		JwtProvider jwtProvider,
    		CustomMentorServiceImpl customMentorServiceImpl,
    		MentorPaymentRepository mentorPaymentRepository,
    		JavaMailSender mailSender) {
        this.mentorRepository = mentorRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtProvider=jwtProvider;
        this.customMentorServiceImpl=customMentorServiceImpl;
        this.mentorPaymentRepository=mentorPaymentRepository;
        this.mailSender=mailSender;
    }
    
	@Override
	public Mentor findMentroById(long mentor_id) throws UserException {
		Optional<Mentor>mentor=mentorRepository.findById(mentor_id);
		if(mentor.isPresent()) {
			return mentor.get();
		}
		throw new UserException("Mentor not found with id "+mentor_id);
	}

	@Override
	public MentorResponse findMentorProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromToken(jwt);
		Mentor mentor=mentorRepository.findByEmailId(email);
		if(mentor==null) {
			throw new UserException("Mentor not found with email "+email);
		}

	    MentorResponse response = mapToMentorResponse(mentor);
		return response;
	}

	@Override
	public Mentor registerMentor(Mentor mentor){
		String email = mentor.getEmailId();
        Mentor existingMentor = mentorRepository.findByEmailId(email);

        if (existingMentor != null) {
        	throw new BadCredentialsException("Email is already used with another account");
        }

        mentor.setPassword(passwordEncoder.encode(mentor.getPassword()));
        Mentor savedMentor = mentorRepository.save(mentor);

        // Create a corresponding MentorPayment record
        MentorPayment mentorPayment = new MentorPayment();
        mentorPayment.setTotalAmount(BigDecimal.ZERO);
        mentorPayment.setCommissionDeducted(BigDecimal.ZERO);
        mentorPayment.setLastPaymentDate(LocalDate.now()); // Payment date initially null
        mentorPayment.setPaymentStatus("due"); // Initial status
        mentorPayment.setPaymentDueDate(getNextMonthFirstDate());
        mentorPayment.setMentor(savedMentor); // Associate with the newly created mentor

        // Save MentorPayment
        mentorPaymentRepository.save(mentorPayment);

        return savedMentor;
	}
	
	private LocalDate getNextMonthFirstDate() {
	    LocalDate today = LocalDate.now();
	    return today.withDayOfMonth(1).plusMonths(1);
	}


	@Override
	public Authentication authenticate(String username, String password) {
		Mentor mentor = mentorRepository.findByEmailId(username);
		if (mentor == null) {
			System.out.println("Invalid Username");
	        throw new BadCredentialsException("Invalid Username");
	    }

	    // Check if the student is blocked
	    if (mentor.getIsBlocked()) {
	    	System.out.println("User is blocked. Please contact support.");
	        throw new BadCredentialsException("User is blocked. Please contact support.");
	    }

	    // Validate the password
	    if (!passwordEncoder.matches(password, mentor.getPassword())) {
	    	System.out.println("Invalid Password");
	        throw new BadCredentialsException("Invalid Password");
	    }
		return new UsernamePasswordAuthenticationToken(
		        new User(mentor.getEmailId(), mentor.getPassword(), new ArrayList<>()),
		        null,
		        new ArrayList<>()
		    );
	}
	
	@Override
	public void setBlockedStatus(Long id, boolean isBlocked) {
        Mentor mentor = mentorRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("QA not found with id: " + id));
        mentor.setIsBlocked(isBlocked);
        mentorRepository.save(mentor);
    }
	
	@Override
	public List<MentorResponse> getAllMentors() {
        List<Mentor> mentors = mentorRepository.findAll();
        return mentors.stream().map(this::mapToMentorResponse).collect(Collectors.toList());
    }

	@Override
    public MentorResponse getMentorById(Long mentorId) throws UserException {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new UserException("Mentor with ID " + mentorId + " not found"));
        return mapToMentorResponse(mentor);
    }
	
	@Override
    public MentorResponse updateMentor(Long mentorId, Mentor updatedMentor) {
        Mentor mentor = mentorRepository.findById(mentorId)
            .orElseThrow(() -> new IllegalArgumentException("Mentor not found with ID: " + mentorId));

        // Update the mentor fields
        mentor.setFirstName(updatedMentor.getFirstName());
        mentor.setLastName(updatedMentor.getLastName());
        mentor.setHeadLine(updatedMentor.getHeadLine());
        mentor.setBio(updatedMentor.getBio());
        mentor.setAreasOfExpertise(updatedMentor.getAreasOfExpertise());
        mentor.setHighestQualification(updatedMentor.getHighestQualification());
        mentor.setPhoneNumber(updatedMentor.getPhoneNumber());
        mentor.setWebsiteLink(updatedMentor.getWebsiteLink());
        mentor.setLinkedInLink(updatedMentor.getLinkedInLink());
        mentor.setYoutubeLink(updatedMentor.getYoutubeLink());
        mentor.setFacebookLink(updatedMentor.getFacebookLink());
        mentor.setImage(updatedMentor.getImage());

        // Save the updated mentor entity
        Mentor savedMentor = mentorRepository.save(mentor);

        // Map the updated mentor entity to a MentorResponse object
        return mapToMentorResponse(savedMentor);
    }

    private MentorResponse mapToMentorResponse(Mentor mentor) {
        MentorResponse response = new MentorResponse();
        response.setMentorId(mentor.getMentorId());
        response.setFirstName(mentor.getFirstName());
        response.setLastName(mentor.getLastName());
        response.setEmailId(mentor.getEmailId());
        response.setPhoneNumber(mentor.getPhoneNumber());
        response.setHeadLine(mentor.getHeadLine());
        response.setImage(mentor.getImage());
        response.setAreasOfExpertise(mentor.getAreasOfExpertise());
        response.setBio(mentor.getBio());
        response.setHighestQualification(mentor.getHighestQualification());
        response.setWebsiteLink(mentor.getWebsiteLink());
        response.setLinkedInLink(mentor.getLinkedInLink());
        response.setYoutubeLink(mentor.getYoutubeLink());
        response.setFacebookLink(mentor.getFacebookLink());
        response.setIsBlocked(mentor.getIsBlocked());
        response.setJoinedDate(mentor.getJoinedDate()); 
        if (mentor.getDrafts() != null ) {
	        response.setDrafts(mentor.getDrafts().stream()
	        	.filter(draft-> "approved".equalsIgnoreCase(draft.getStatus()))
	            .map(draft -> {
	                DraftResponse draftResponse = new DraftResponse();
	                draftResponse.setSellingPrice(draft.getSellingPrice());
	                draftResponse.setThumbnailUrl(draft.getThumbnailUrl());
	                draftResponse.setLevel(draft.getLevel());
	                draftResponse.setDraftId(draft.getDraftId());
	                draftResponse.setCourseName(draft.getCourseName());
	                draftResponse.setCourseDescription(draft.getCourseDescription());
	                draftResponse.setCoursePrice(draft.getCoursePrice());
	                return draftResponse;
	            }).collect(Collectors.toList()));
	    }

	    return response;
    }

	public void sendPasswordResetToken(String email) throws UserException {
        Mentor mentor = mentorRepository.findByEmailId(email);	        
        if (mentor == null) {
            throw new UserException("No account found with this email address.");
        }
        if (mentor.getIsBlocked()==true) {
            throw new UserException("User is blocked. Please contact support.");
        }
        String token = UUID.randomUUID().toString();
        mentor.setResetPasswordToken(token);
        mentorRepository.save(mentor);

        // Send the reset link via email
        String resetLink = "https://www.anshitha.cloud/mentor/reset-password?token=" + token;
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
        Mentor mentor = mentorRepository.findByResetPasswordToken(token);

        if (mentor == null) {
            throw new UserException("Invalid or expired reset token.");
        }
        
        if (passwordEncoder.matches(newPassword, mentor.getPassword())) {
            throw new UserException("New password cannot be the same as the previous password.");
        }
        
        mentor.setPassword(passwordEncoder.encode(newPassword)); 
        mentor.setResetPasswordToken(null); // Clear the token
        mentorRepository.save(mentor);
    }
}
