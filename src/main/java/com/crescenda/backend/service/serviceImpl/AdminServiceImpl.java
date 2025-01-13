package com.crescenda.backend.service.serviceImpl;

import java.util.ArrayList;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crescenda.backend.config.JwtProvider;
import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Admin;
import com.crescenda.backend.repository.AdminRepository;
import com.crescenda.backend.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{

	private AdminRepository adminRepository;
	private PasswordEncoder passwordEncoder;
	private JwtProvider jwtProvider;
	
	
	public AdminServiceImpl(AdminRepository adminRepository,
			PasswordEncoder passwordEncoder,
			JwtProvider jwtProvider) {
		this.adminRepository = adminRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtProvider=jwtProvider;
	}


	public Authentication authenticateAdmin(String email, String rawPassword) {
	    Admin admin = adminRepository.findByAdminUsername(email);
	    if (admin == null) {
	        throw new BadCredentialsException("Invalid Username");
	    }
	    if (!passwordEncoder.matches(rawPassword, admin.getAdminPassword())) {
	        throw new BadCredentialsException("Invalid password");
	    }
	    return new UsernamePasswordAuthenticationToken(
	        new User(admin.getAdminUsername(), admin.getAdminPassword(), new ArrayList<>()),
	        null,
	        new ArrayList<>()
	    );
	}

	
	@Override
	public Admin findAdminProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromToken(jwt);
		Admin admin=adminRepository.findByAdminUsername(email);
		if(admin==null) {
			throw new UserException("Mentor not found with email "+email);
		}
		return admin;
	}

}
