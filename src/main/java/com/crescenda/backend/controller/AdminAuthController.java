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
import com.crescenda.backend.model.Admin;
import com.crescenda.backend.request.LoginRequest;
import com.crescenda.backend.response.AuthResponse;
import com.crescenda.backend.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminAuthController {
	
	private AdminService adminService;
	private JwtProvider jwtProvider;
	
	public AdminAuthController(AdminService adminService, JwtProvider jwtProvider) {
        this.adminService = adminService;
        this.jwtProvider = jwtProvider;
    }

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginAdmin(@RequestBody LoginRequest loginRequest) {
		try {
	    Authentication authentication = adminService.authenticateAdmin(
	        loginRequest.getEmail(),
	        loginRequest.getPassword()
	    );

	    // Generate JWT token after authentication
	    SecurityContextHolder.getContext().setAuthentication(authentication);
	    String token = jwtProvider.generateToken(authentication);

	    // Prepare the response
	    AuthResponse authResponse = new AuthResponse();
	    authResponse.setJwt(token);
	    authResponse.setMessage("Admin SignIn Success");
	    return new ResponseEntity<>(authResponse, HttpStatus.OK);
		} catch (BadCredentialsException ex) {
	        return new ResponseEntity<>(new AuthResponse(null, ex.getMessage()), HttpStatus.UNAUTHORIZED);
	    }
	}

    
    @GetMapping("/profile")
	public ResponseEntity<Admin>getStudentProfileHandler(@RequestHeader("Authorization")String jwt)throws UserException{
		Admin admin=adminService.findAdminProfileByJwt(jwt);
		return new ResponseEntity<Admin>(admin,HttpStatus.ACCEPTED);
	}
}
