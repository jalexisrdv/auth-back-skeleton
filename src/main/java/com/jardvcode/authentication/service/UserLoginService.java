package com.jardvcode.authentication.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.jardvcode.shared.domain.exception.ApplicationException;
import com.jardvcode.shared.domain.exception.GeneralServiceException;
import com.jardvcode.shared.util.JwtUtil;

@Service
public class UserLoginService {
	
	private final static Logger LOG = LoggerFactory.getLogger("authenticationLog");
	
	private final AuthenticationManager authenticationManager;
	
	public UserLoginService(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	public String logIn(String username, String password) {
		try {
			Authentication unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
			Authentication authentication = authenticationManager.authenticate(unauthenticated);
			
			if (!authentication.isAuthenticated()) throw new ApplicationException("");
			
			return JwtUtil.generateToken(username);
		} catch (AuthenticationException | ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw new ApplicationException("Incorrect email or password. Please try again.");
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			throw new GeneralServiceException();
		}
	}

}
