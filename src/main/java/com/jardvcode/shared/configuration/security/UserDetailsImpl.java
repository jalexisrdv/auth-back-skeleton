package com.jardvcode.shared.configuration.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jardvcode.user.entity.UserEntity;

public class UserDetailsImpl implements UserDetails, CredentialsContainer {
	
	private String password;
	private String username;
	private List<GrantedAuthority> authorities;

	public UserDetailsImpl(UserEntity user) {
		this.password = user.getPassword();
		this.username = user.getUsername();
		this.authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
    public void eraseCredentials() {
        this.password = null;
    }
	
}
