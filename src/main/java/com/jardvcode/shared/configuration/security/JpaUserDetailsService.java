package com.jardvcode.shared.configuration.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.jardvcode.user.entity.UserEntity;
import com.jardvcode.user.repository.UserRepository;

public class JpaUserDetailsService implements UserDetailsService {
	
	private UserRepository repository;
	
	public JpaUserDetailsService(UserRepository repository) {
		this.repository = repository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
		return new UserDetailsImpl(user);
	}

}
