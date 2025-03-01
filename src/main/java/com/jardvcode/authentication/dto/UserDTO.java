package com.jardvcode.authentication.dto;

public record UserDTO(
		String email,
		String password,
		String verificationCode
) {}