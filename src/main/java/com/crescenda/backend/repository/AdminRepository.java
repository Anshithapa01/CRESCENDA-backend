package com.crescenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{
	
	public Admin findByAdminUsername(String adminUsername);
}
