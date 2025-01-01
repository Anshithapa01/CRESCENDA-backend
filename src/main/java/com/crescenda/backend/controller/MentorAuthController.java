package com.crescenda.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.request.LoginRequest;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.service.MentorService;

@RestController
@RequestMapping("/mentor/auth")
public class MentorAuthController {
	
	private JwtProvider jwtProvider;
	private final MentorService mentorService;
	
	public MentorAuthController(
			JwtProvider jwtProvider,
			MentorService mentorService) {
		
		this.jwtProvider=jwtProvider;
		this.mentorService=mentorService;
	}
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>createUserHandler(@RequestBody Mentor mentor)throws UserException{
		try {
			Mentor savedMentor=mentorService.registerMentor(mentor);
			Authentication authentication=new UsernamePasswordAuthenticationToken(savedMentor.getEmailId(), savedMentor.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token=jwtProvider.generateToken(authentication);
			AuthResponse authResponse=new AuthResponse();
			authResponse.setJwt(token);
			authResponse.setMessage("Signup Success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		} catch (BadCredentialsException ex) {
	        return new ResponseEntity<>(new AuthResponse(null, ex.getMessage()), HttpStatus.UNAUTHORIZED);
	    }
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest login)throws UserException{
		try {
			Authentication authentication=mentorService.authenticate(login.getEmail(),login.getPassword());
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token=jwtProvider.generateToken(authentication);
			AuthResponse authResponse=new AuthResponse();
			authResponse.setJwt(token);
			authResponse.setMessage("SignIn Success");
			return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
		} catch (BadCredentialsException ex) {
	        return new ResponseEntity<>(new AuthResponse(null, ex.getMessage()), HttpStatus.UNAUTHORIZED);
	    }
	}
	
	
	@PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) throws UserException {
        String email = request.get("email");
        mentorService.sendPasswordResetToken(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) throws UserException {
	    String token = request.get("token");
	    String password = request.get("password");
	    System.out.println("token :"+token+" password :"+password);
	    mentorService.resetPassword(token, password);
	    return ResponseEntity.ok("Password has been reset successfully.");
	}

}
