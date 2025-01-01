package com.crescenda.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Admin {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "admin_id")
    private Long adminId;

    @Column(name = "admin_username")
    private String adminUsername;

    @Column(name = "admin_password")
    private String adminPassword;

    @Column(name = "role")
    private String role="ADMIN";

	public Admin(Long adminId, String adminUsername, String adminPassword, String role) {
		super();
		this.adminId = adminId;
		this.adminUsername = adminUsername;
		this.adminPassword = adminPassword;
		this.role = role;
	} 

    

}
