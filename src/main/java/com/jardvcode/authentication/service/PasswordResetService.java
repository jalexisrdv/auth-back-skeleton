package com.jardvcode.authentication.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jardvcode.shared.domain.exception.ApplicationException;
import com.jardvcode.shared.domain.exception.GeneralServiceException;
import com.jardvcode.shared.util.PasswordUtil;
import com.jardvcode.user.entity.UserEntity;
import com.jardvcode.user.repository.UserRepository;

@Service
public class PasswordResetService {
	
	private final static Logger LOG = LoggerFactory.getLogger("authenticationLog");
	
	private final UserRepository repository;
	
	public PasswordResetService(UserRepository userRepository) {
		this.repository = userRepository;
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
