package com.crescenda.backend.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();

	    // Add unique origins
	    configuration.setAllowedOrigins(Arrays.asList(
	        "https://www.anshitha.cloud",
	        "https://anshitha.cloud",
	        "http://localhost:5173",
	        "http://crescenda.s3-website.ap-south-1.amazonaws.com"
	    ));
	    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
	    configuration.setExposedHeaders(Arrays.asList("Authorization"));
	    configuration.setAllowCredentials(true); // Required for cookies/authentication headers
	    configuration.setMaxAge(3600L);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable)
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
	            .requestMatchers("/mentor","/auth/signup").permitAll() 
	            .requestMatchers("/auth/**", "/auth/signin").permitAll()
	            .requestMatchers("/api/**").authenticated()            // Require authentication for /api/*
	            .anyRequest().permitAll()                              // Default permit for other endpoints
	        )
	        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class); // JWT validation
	    return http.build();
	}

	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
	}
}
