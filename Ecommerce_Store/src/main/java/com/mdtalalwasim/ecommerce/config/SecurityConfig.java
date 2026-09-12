package com.mdtalalwasim.ecommerce.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {
	
	@Autowired
	AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	@Lazy
	AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	@Lazy
	AuthenticationFailureHandler adminAuthenticationFailureHandler;
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}
	
	//for authentication : userDetails and Password
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
		
	}
	
	//Admin login chain : separate dark login page (/admin-login) with its own processing URL
	@Bean
	public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/admin/**", "/admin-login", "/admin-login-process", "/admin-logout")
		.csrf(csrf -> csrf.disable())
		.cors(cors->cors.disable())
		.authorizeHttpRequests(req-> req.requestMatchers("/admin-login", "/admin-logout").permitAll()
				.requestMatchers("/admin/**").hasRole("ADMIN"))
		.formLogin(form-> form.loginPage("/admin-login")
				.loginProcessingUrl("/admin-login-process")
				.failureHandler(adminAuthenticationFailureHandler)
				.successHandler(authenticationSuccessHandler))
		.logout(logout->logout.logoutUrl("/admin-logout").logoutSuccessUrl("/admin-login?logout").permitAll());
		return http.build();
		
	}
	
	//which role can get which access : default chain for storefront/user
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		.cors(cors->cors.disable())
		.authorizeHttpRequests(req-> req.requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
		.requestMatchers("/admin/**").hasRole("ADMIN")
		.requestMatchers("/**").permitAll())
		.formLogin(form-> form.loginPage("/signin")
				.loginProcessingUrl("/login")
				//.defaultSuccessUrl("/")//before implements authenticationsSuccessHandler.
				//after implementation authenticationsSuccessHandler -> call successHandler 
				.failureHandler(authenticationFailureHandler)
				.successHandler(authenticationSuccessHandler))
				
		.logout(logout->logout.logoutSuccessUrl("/signin?logout").permitAll());
		return http.build();
		
	}
	
	
}
