package com.crescenda.backend.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.crescenda.backend.model.Mentor;
import com.crescenda.backend.repository.MentorRepository;

@Service
public class CustomMentorServiceImpl implements UserDetailsService{

	    private MentorRepository mentorRepository;

	    public CustomMentorServiceImpl(MentorRepository mentorRepository) {
	        this.mentorRepository = mentorRepository;
	    }

	    @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Mentor mentor = mentorRepository.findByEmailId(username);
	        if (mentor == null) {
	            throw new UsernameNotFoundException("Mentor not found with email " + username);
	        }

	        List<GrantedAuthority> authorities = new ArrayList<>();

	        return new User(mentor.getEmailId(), mentor.getPassword(), authorities);
	    }
}
