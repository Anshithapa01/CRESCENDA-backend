package com.crescenda.backend.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.ResourceNotFoundException;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.repository.QaRepository;
import com.crescenda.backend.request.QARequest;
import com.crescenda.backend.response.QAResponse;
import com.crescenda.backend.service.QaService;

@Service
public class QaServiceImpl implements QaService{

	private QaRepository qaRepository;
	private PasswordEncoder passwordEncoder;
	private JwtProvider jwtProvider;
	
	
	public QaServiceImpl(QaRepository qaRepository,
			PasswordEncoder passwordEncoder,
			JwtProvider jwtProvider) {
		this.qaRepository = qaRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider=jwtProvider;
	}


	@Override
	public Authentication authenticateQA(String email, String rawPassword) {
		QA qa = qaRepository.findByEmailId(email);
		System.out.println("email :"+email +" password :"+rawPassword );
		if (qa == null) {
		    System.out.println("Invalid Username");
		    throw new BadCredentialsException("Invalid UserName");
		}

		if (!qa.getRole().equals("QA_Lead")) {
		    System.out.println("Invalid Role");
		    throw new BadCredentialsException("Invalid QA credentials");
		}
        if (qa.getIsBlocked()) {
	    	System.out.println("User is blocked. Please contact support.");
	        throw new BadCredentialsException("User is blocked. Please contact support.");
	    }
        if (!passwordEncoder.matches(rawPassword, qa.getPassword())) {
        	System.out.println("Invalid Password");
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(
		        new User(qa.getEmailId(), qa.getPassword(), new ArrayList<>()),
		        null,
		        new ArrayList<>()
		    );
	}
	
	@Override
	public QA findQAProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromToken(jwt);
		QA qa=qaRepository.findByEmailId(email);
		if(qa==null) {
			throw new UserException("Mentor not found with email "+email);
		}
		return qa;
	}
	
	@Override
	public void setBlockedStatus(Long id, boolean isBlocked) {
        QA qa = qaRepository.findById(id).orElseThrow(() -> 
            new ResourceNotFoundException("QA not found with id: " + id));
        qa.setIsBlocked(isBlocked);
        qaRepository.save(qa);
    }
	
	@Override
	public List<QAResponse> getAllQAs() {
        return qaRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

	@Override
	public QA saveQA(QARequest qaRequest) throws UserException {
		 QA newQA = new QA();
	        newQA.setFirstName(qaRequest.getFirstName());
	        newQA.setLastName(qaRequest.getLastName());
	        newQA.setEmailId(qaRequest.getEmailId());
	        newQA.setPhoneNumber(qaRequest.getPhoneNumber());
	        newQA.setRole(qaRequest.getRole());
	        newQA.setQualification(qaRequest.getQualification());
	        newQA.setExperience(qaRequest.getExperience());
	        newQA.setAreasOfExpertise(qaRequest.getAreasOfExpertise());
	        
	        if (qaRequest.getLead() != null) {
	            QA lead = qaRepository.findById(qaRequest.getLead().getQaId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));
	            newQA.setLead(lead);
	        }
	        System.out.println("qaRequest.getPassword()"+qaRequest.getPassword());
	        if(qaRequest.getRole().equals("QA_Lead")) {
	        	String encryptedPassword = passwordEncoder.encode(qaRequest.getPassword());
		        newQA.setPassword(encryptedPassword);
	        }
	    if (qaRepository.findByEmailId(qaRequest.getEmailId()) != null) {
	        throw new UserException("A QA with this email already exists: " + qaRequest.getEmailId());
	    }
	    String rolePrefix = qaRequest.getRole().equalsIgnoreCase("QA_Expert") ? "QE" : "QL";
	    String userProvidedDigits = String.format("%04d", new Random().nextInt(10000)); 
	    String nameInitials = (qaRequest.getFirstName().charAt(0) + "" + qaRequest.getLastName().charAt(0)).toUpperCase();
	    String randomSuffix = generateRandomString(3); 
	    String qaUid = rolePrefix + userProvidedDigits + nameInitials + randomSuffix;
	    newQA.setQaUid(qaUid);
	    return qaRepository.save(newQA); 
	}
	
	@Override
	 public QA updateQA(Long id, QARequest qaDetails) {
	        QA qa = qaRepository.findById(id).orElseThrow(() -> new RuntimeException("QA not found"));
	        qa.setFirstName(qaDetails.getFirstName());
	        qa.setLastName(qaDetails.getLastName());
	        qa.setRole(qaDetails.getRole());
	        qa.setEmailId(qaDetails.getEmailId());
	        qa.setPhoneNumber(qaDetails.getPhoneNumber());
	        qa.setQualification(qaDetails.getQualification());
	        qa.setExperience(qaDetails.getExperience());
	        qa.setAreasOfExpertise(qaDetails.getAreasOfExpertise());
	        if (qaDetails.getLead() != null&& qaDetails.getLead().getQaId() != null) {
	            QA lead = qaRepository.findById(qaDetails.getLead().getQaId())
	                    .orElseThrow(() -> new ResourceNotFoundException("Lead not found"));
	            qa.setLead(lead);
	        }else if(qaDetails.getLead().getQaId()==null) {
	        	qa.setLead(null);
	        }
	        if(qaDetails.getRole().equals("QA_Lead")) {
	        	String encryptedPassword = passwordEncoder.encode(qaDetails.getPassword());
	        	qa.setPassword(encryptedPassword);
	        }
	        return qaRepository.save(qa);
	    }
	 
	 @Override
	 public QAResponse getQAById(Long id) {
	        return (QAResponse) qaRepository.findById(id).stream().map(this::convertToResponse).collect(Collectors.toList());
	    }
	 
	 @Override
	 public List<QAResponse> getAllQAByRole(String role) {
	     return qaRepository.findByRole(role)
	             .stream()
	             .map(this::convertToResponse)
	             .collect(Collectors.toList());
	 }
	 
	@Override
	public QAResponse convertToResponse(QA qa) {
        QAResponse response = new QAResponse();
        response.setQaId(qa.getQaId());
        response.setFirstName(qa.getFirstName());
        response.setLastName(qa.getLastName());
        response.setEmailId(qa.getEmailId());
        response.setPhoneNumber(qa.getPhoneNumber());
        response.setRole(qa.getRole());
        response.setQualification(qa.getQualification());
        response.setExperience(qa.getExperience());
        response.setDateOfJoin(qa.getDateOfJoin());
        response.setTaskCount(qa.getTaskCount());
        response.setAreasOfExpertise(qa.getAreasOfExpertise());
        response.setQaUid(qa.getQaUid());
        if(qa.getLead()!=null) {
        	response.setLead(qa.getLead());
        }
        
        response.setIsBlocked(qa.getIsBlocked());
        return response;
    }
	
	private String generateRandomString(int length) {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    Random random = new Random();
	    StringBuilder result = new StringBuilder();
	    for (int i = 0; i < length; i++) {
	        result.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    return result.toString();
	}
}
