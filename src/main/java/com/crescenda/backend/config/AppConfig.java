package com.crescenda.backend.config;

import java.util.Arrays;

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
//	    CorsConfiguration configuration = new CorsConfiguration();
//	    configuration.setAllowedOrigins(Arrays.asList("https://www.anshitha.cloud"));
//	    configuration.setAllowedMethods(Arrays.asList("OPTIONS", "HEAD", "GET", "POST", "PUT", "DELETE"));
//	    configuration.setAllowedHeaders(Arrays.asList(
//                "X-Requested-With", "X-Auth-Token", "Content-Type", "Content-Length", 
//                "Authorization", "Access-Control-Allow-Headers", "Accept", 
//                "Access-Control-Allow-Methods", "Access-Control-Allow-Origin", 
//                "Access-Control-Allow-Credentials"
//        ));
//	    configuration.setAllowCredentials(true);
//
//	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//	    source.registerCorsConfiguration("/**", configuration); // Apply CORS to all endpoints
//	    return source;
		
		CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowCredentials(true);
	    configuration.addAllowedOrigin("https://www.anshitha.cloud");
	    configuration.addAllowedHeader("*");
	    configuration.addAllowedMethod("*");

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
	            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Allow preflight requests
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
