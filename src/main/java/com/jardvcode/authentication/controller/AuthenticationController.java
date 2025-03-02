package com.jardvcode.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jardvcode.authentication.dto.UserDTO;
import com.jardvcode.authentication.mapper.UserMapper;
import com.jardvcode.authentication.service.PasswordResetService;
import com.jardvcode.authentication.service.UserLoginService;
import com.jardvcode.authentication.service.UserRegistrationService;
import com.jardvcode.authentication.service.UserVerifierService;
import com.jardvcode.shared.domain.response.ResponseWrapper;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {
	
	private final UserRegistrationService userRegistrationService;
	private final UserLoginService userLoginService;
	private final PasswordResetService passwordResetService;
	private final UserVerifierService userVerifier;
	private final UserMapper mapper;
	
	public AuthenticationController(UserRegistrationService userRegistrationService, UserLoginService userLoginService,
			PasswordResetService passwordResetService, UserVerifierService userVerifier) {
		this.userRegistrationService = userRegistrationService;
		this.userLoginService = userLoginService;
		this.passwordResetService = passwordResetService;
		this.userVerifier = userVerifier;
		mapper = new UserMapper();
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseWrapper<String>> logIn(@RequestBody UserDTO dto) {
		String token = userLoginService.logIn(dto.email(), dto.password());
		return ResponseWrapper.ok(token);
	}
	
	@GetMapping("/register/send-code")
	public ResponseEntity<ResponseWrapper<Void>> sendRegisterCode(@RequestParam String recipient) {
		userVerifier.sendRegisterCode(recipient);
		return ResponseWrapper.ok(null);
	}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseWrapper<UserDTO>> register(@RequestBody UserDTO dto) {
		UserDTO user = mapper.fromEntity(userRegistrationService.register(mapper.fromDTO(dto)));
		return ResponseWrapper.ok(user);
	}
	
	@GetMapping("/forgot-password/send-code")
	public ResponseEntity<ResponseWrapper<Void>> sendPasswordResetCode(@RequestParam String recipient) {
		userVerifier.sendPasswordResetCode(recipient);
		return ResponseWrapper.ok(null);
	}
	
	@GetMapping("/forgot-password/validate-code")
	public ResponseEntity<ResponseWrapper<Void>> validatePasswordResetCode(@RequestParam String recipient, @RequestParam String verificationCode) {
		userVerifier.validatePasswordResetCode(recipient, verificationCode);
		return ResponseWrapper.ok(null);
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<ResponseWrapper<UserDTO>> resetPassword(@RequestBody UserDTO dto) {
		UserDTO user = mapper.fromEntity(passwordResetService.resetPassword(mapper.fromDTO(dto)));
		return ResponseWrapper.ok(user, "Password change completed. You can now log in with your new password.");
	}
	
}
