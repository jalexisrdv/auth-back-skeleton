package com.jardvcode.authentication.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.jardvcode.shared.domain.exception.ApplicationException;
import com.jardvcode.shared.domain.exception.GeneralServiceException;
import com.jardvcode.shared.util.JwtUtil;
import com.jardvcode.shared.util.PasswordUtil;
import com.jardvcode.user.entity.AuthorityEntity;
import com.jardvcode.user.entity.UserEntity;
import com.jardvcode.user.repository.UserRepository;

public class AuthenticationService {
	
	private final static Logger LOG = LoggerFactory.getLogger("authenticationLog");
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository repository;
	
	public AuthenticationService(AuthenticationManager authenticationManager, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.repository = userRepository;
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
	
	public UserEntity register(UserEntity entity) {
		try {
			UserEntity userEntityFound = repository.findByUsernameAndVerificationCode(entity.getUsername(), entity.getVerificationCode())
					.orElseThrow(() -> new ApplicationException("Invalid verification code. Please try again"));
			
			boolean verificationCodeExpired = LocalDateTime.now().isAfter(userEntityFound.getVerificationCodeExpirationDate());
			if(verificationCodeExpired) throw new ApplicationException("Invalid verification code. Please try again");
			
			if(userEntityFound.isEnabled()) throw new ApplicationException("Oops! This email is already associated with an account.");
			
			AuthorityEntity authorityEntity = new AuthorityEntity();
			authorityEntity.setUsername(entity.getUsername());
			authorityEntity.setAuthority("USER");
			
			List<AuthorityEntity> authorities = new ArrayList<>();
			authorities.add(authorityEntity);
			
			userEntityFound.setPassword(PasswordUtil.hashPassword(entity.getPassword()));
			userEntityFound.setEnabled(true);
			
			userEntityFound.setAuthorities(authorities);
			userEntityFound.setVerificationCode(null);
			userEntityFound.setVerificationCodeExpirationDate(null);
			
			UserEntity entitySaved = repository.save(userEntityFound);
			
			return entitySaved;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			throw new GeneralServiceException();
		}
	}
	
	public UserEntity resetPassword(UserEntity entity) {
		try {
			UserEntity userEntityFound = repository.findByUsernameAndVerificationCode(entity.getUsername(), entity.getVerificationCode())
					.orElseThrow(() -> new ApplicationException("Invalid verification code. Please try again"));
			
			boolean verificationCodeExpired = LocalDateTime.now().isAfter(userEntityFound.getVerificationCodeExpirationDate());
			
			if(verificationCodeExpired) throw new ApplicationException("Invalid verification code. Please try again");
			
			userEntityFound.setPassword(PasswordUtil.hashPassword(entity.getPassword()));
			userEntityFound.setVerificationCode(null);
			userEntityFound.setVerificationCodeExpirationDate(null);
			
			UserEntity entitySaved = repository.save(userEntityFound);
			
			return entitySaved;
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			throw new GeneralServiceException();
		}
	}

}
