package com.crescenda.backend.service;

import org.springframework.security.core.Authentication;

import com.crescenda.backend.exception.UserException;
import com.crescenda.backend.model.Admin;;

public interface AdminService {
	
	public Authentication authenticateAdmin(String email, String rawPassword);

	Admin findAdminProfileByJwt(String jwt) throws UserException;
	
}
