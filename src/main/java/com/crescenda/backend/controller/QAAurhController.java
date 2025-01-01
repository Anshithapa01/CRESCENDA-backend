package com.crescenda.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.QA;
import com.crescenda.backend.request.LoginRequest;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.service.QaService;

@RestController
@RequestMapping("/qa")
public class QAAurhController {

	private QaService qaService;
	private JwtProvider jwtProvider;
	
	public QAAurhController(QaService qaService, JwtProvider jwtProvider) {
        this.qaService = qaService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginQA(@RequestBody LoginRequest loginRequest) throws UserException{
    	try {
    		Authentication authentication=qaService.authenticateQA(loginRequest.getEmail(), loginRequest.getPassword());
//	        Authentication authentication = new UsernamePasswordAuthenticationToken(
//	                loginRequest.getEmail(), null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
//	        );
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String token = jwtProvider.generateToken(authentication);
	        AuthResponse authResponse = new AuthResponse();
	        authResponse.setJwt(token);
	        authResponse.setMessage("QA SignIn Success");
	        return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
    	} catch (BadCredentialsException ex) {
	        return new ResponseEntity<>(new AuthResponse(null, ex.getMessage()), HttpStatus.UNAUTHORIZED);
	    }
    }
    
    @GetMapping("/profile")
	public ResponseEntity<QA>getStudentProfileHandler(@RequestHeader("Authorization")String jwt)throws UserException{
		QA qa=qaService.findQAProfileByJwt(jwt);
		return new ResponseEntity<QA>(qa,HttpStatus.ACCEPTED);
	}
}
