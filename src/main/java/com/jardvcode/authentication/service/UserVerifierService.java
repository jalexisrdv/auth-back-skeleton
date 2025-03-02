package com.jardvcode.authentication.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jardvcode.messagesender.service.EmailSenderService;
import com.jardvcode.shared.domain.BusinessRules;
import com.jardvcode.shared.domain.exception.ApplicationException;
import com.jardvcode.shared.domain.exception.GeneralServiceException;
import com.jardvcode.shared.domain.exception.NotDataFoundException;
import com.jardvcode.shared.util.OTPCodeGenerator;
import com.jardvcode.user.entity.UserEntity;
import com.jardvcode.user.repository.UserRepository;

@Service
public class UserVerifierService {

	private final static Logger LOG = LoggerFactory.getLogger("authenticationLog");

	private final UserRepository repository;
	private final EmailSenderService messageSender;

	public UserVerifierService(UserRepository repository, EmailSenderService messageSender) {
		this.repository = repository;
		this.messageSender = messageSender;
	}

	public void sendRegisterCode(String email) {
		try {
			repository.findByUsername(email).ifPresent((user) -> {
				if (user.isEnabled()) throw new ApplicationException("Oops! This email is already associated with an account.");
			});

			String verificationCode = OTPCodeGenerator.generateNumericOTP(6);

			UserEntity user = new UserEntity();

			user.setUsername(email);
			user.setEnabled(false);
			user.setVerificationCode(verificationCode);
			user.setVerificationCodeExpirationDate(LocalDateTime.now().plusMinutes(BusinessRules.VERIFICATION_CODE_EXPIRATION_MINUTES_FOR_REGISTRATION));

			repository.save(user);

			String subject = "Verify your account";
			String text = "Your verification code is: " + verificationCode + ", this code expire in " + BusinessRules.VERIFICATION_CODE_EXPIRATION_MINUTES_FOR_REGISTRATION + " minutes";
			messageSender.send(email, subject, text);
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
			throw new GeneralServiceException();
		}
	}
	
	public void sendPasswordResetCode(String email) {
		try {
			UserEntity userEntityFound = repository.findByUsername(email).orElseThrow(() -> new NotDataFoundException("User not found."));
			
			String verificationCode = OTPCodeGenerator.generateNumericOTP(6);
			
			userEntityFound.setVerificationCode(verificationCode);
			userEntityFound.setVerificationCodeExpirationDate(LocalDateTime.now().plusMinutes(BusinessRules.VERIFICATION_CODE_EXPIRATION_MINUTES_FOR_PASSWORD_RESET));

			repository.save(userEntityFound);

			String subject = "Verify your account";
			String text = "Your verification code is: " + verificationCode + ", this code expire in " + BusinessRules.VERIFICATION_CODE_EXPIRATION_MINUTES_FOR_PASSWORD_RESET + " minutes";
			messageSender.send(email, subject, text);
		} catch(NotDataFoundException e) {
			LOG.info(e.getMessage(), e);
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
		}
	}
	
	public void validatePasswordResetCode(String email, String verificationCode) {
		try {
			UserEntity userEntityFound = repository.findByUsernameAndVerificationCode(email, verificationCode)
					.orElseThrow(() -> new ApplicationException("Invalid verification code. Please try again"));
			
			boolean verificationCodeExpired = LocalDateTime.now().isAfter(userEntityFound.getVerificationCodeExpirationDate());
			
			if(verificationCodeExpired) throw new ApplicationException("Invalid verification code. Please try again");
		} catch (ApplicationException e) {
			LOG.info(e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOG.info(e.getMessage(), e);
		}
	}

}
