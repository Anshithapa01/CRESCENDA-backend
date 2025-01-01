package com.crescenda.backend.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Student;
import com.crescenda.backend.repository.StudentRepository;

@Service
public class CustomUserServiceImplementation implements UserDetailsService{

	private StudentRepository studentRepository;
	
	public CustomUserServiceImplementation(StudentRepository studentRepository) {
		this.studentRepository=studentRepository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Student student=studentRepository.findByEmailId(username);
		if(student==null) {
			throw new UsernameNotFoundException("Student not found with email "+username);
		}
		
		List<GrantedAuthority> authorities=new ArrayList<>();
		
		return new User(student.getEmailId(),student.getPassword(),authorities);
	}
	

}
