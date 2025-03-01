package com.jardvcode.authentication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jardvcode.authentication.dto.UserDTO;
import com.jardvcode.authentication.mapper.UserMapper;
import com.jardvcode.authentication.service.AuthenticationService;
import com.jardvcode.authentication.service.UserVerifierService;
import com.jardvcode.messagesender.service.EmailSenderService;
import com.jardvcode.shared.domain.response.ResponseWrapper;
import com.jardvcode.user.repository.UserRepository;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "auth")
public class AuthenticationController {
	
	private final AuthenticationService authenticationService;
	private final UserVerifierService userVerifier;
	private final UserMapper mapper;

	@Autowired
	public AuthenticationController(AuthenticationManager authenticationManager, UserRepository repository, JavaMailSender sender) {
		authenticationService = new AuthenticationService(authenticationManager, repository);
		userVerifier = new UserVerifierService(repository, new EmailSenderService(sender));
		mapper = new UserMapper();
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseWrapper<String>> signIn(@RequestBody UserDTO dto) {
		String token = authenticationService.logIn(dto.email(), dto.password());
		return ResponseWrapper.ok(token);
	}
	
	@GetMapping("/register/send-code")
	public ResponseEntity<ResponseWrapper<Void>> sendSignUpVerificationCode(@RequestParam(value = "recipient") String recipient) {
		userVerifier.sendRegisterCode(recipient);
		return ResponseWrapper.ok(null);
	}
	
	@PostMapping("/register")
	public ResponseEntity<ResponseWrapper<UserDTO>> signUp(@RequestBody UserDTO dto) {
		UserDTO user = mapper.fromEntity(authenticationService.register(mapper.fromDTO(dto)));
		return ResponseWrapper.ok(user);
	}
	
	@GetMapping("/forgot-password/send-code")
	public ResponseEntity<ResponseWrapper<Void>> sendPasswordResetCode(@RequestParam(value = "recipient") String recipient) {
		userVerifier.sendPasswordResetCode(recipient);
		return ResponseWrapper.ok(null);
	}
	
	@GetMapping("/forgot-password/validate-code")
	public ResponseEntity<ResponseWrapper<Void>> validatePasswordResetCode(@RequestParam(value = "recipient") String recipient, @RequestParam(value = "verificationCode") String verificationCode) {
		userVerifier.validatePasswordResetCode(recipient, verificationCode);
		return ResponseWrapper.ok(null);
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<ResponseWrapper<UserDTO>> resetPassword(@RequestBody UserDTO dto) {
		UserDTO user = mapper.fromEntity(authenticationService.resetPassword(mapper.fromDTO(dto)));
		return ResponseWrapper.ok(user, "Password change completed. You can now log in with your new password.");
	}
	
}
