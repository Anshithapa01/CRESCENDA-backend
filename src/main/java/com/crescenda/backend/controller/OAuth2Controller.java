package com.crescenda.backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Student;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.service.StudentService;


@RestController
public class OAuth2Controller {
	
	private JwtProvider jwtProvider;
	private final StudentService studentService;
	
	public OAuth2Controller(
			JwtProvider jwtProvider,
			StudentService studentService) {
		this.jwtProvider=jwtProvider;
		this.studentService=studentService;
	}

    @GetMapping("/user")
    public OAuth2User user(@AuthenticationPrincipal OAuth2User principal) {
    	System.out.println("helllllloooooooooooooooooooooooooooo"); 
        return principal;
    }
    
    @PostMapping("/oauth2/login")
    public ResponseEntity<AuthResponse> handleOAuth2Login(@RequestBody Map<String, String> payload) {
    	System.out.println(payload.get("token"));
        String accessToken = payload.get("token");

        try {
            Student student = studentService.verifyGoogleAccessToken(accessToken);

            // Generate JWT token
            String jwtToken = jwtProvider.generateToken(new UsernamePasswordAuthenticationToken(student.getEmailId(), null));

            return new ResponseEntity<>(new AuthResponse(jwtToken, "Login Success"), HttpStatus.OK);
        } catch (UserException e) {
            return new ResponseEntity<>(new AuthResponse(null, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
    
    @PostMapping("/oauth2/signup")
    public ResponseEntity<AuthResponse> googleSignupHandler(@RequestBody Map<String, String> tokenData) {
        try {
            String idToken = tokenData.get("token");
            AuthResponse response = studentService.signupWithGoogle(idToken);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse(null, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse(null, "Signup failed: " + e.getMessage()));
        }
    }


}