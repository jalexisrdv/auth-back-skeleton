package com.jardvcode.shared.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String hashPassword(String password) {
		return passwordEncoder.encode(password);
	}

	public static boolean checkPassword(String password, String hashed) {
		return passwordEncoder.matches(password, hashed);
	}

}
