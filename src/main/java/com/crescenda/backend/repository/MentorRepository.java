package com.crescenda.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.crescenda.backend.model.Mentor;

public interface MentorRepository extends JpaRepository<Mentor, Long>{
	public Mentor findByEmailId(String emailId);

	public Mentor findByResetPasswordToken(String token);
}
