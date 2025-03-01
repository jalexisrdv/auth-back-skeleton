package com.jardvcode.shared.util;

import java.security.SecureRandom;

public class OTPCodeGenerator {
	
	private static final SecureRandom random = new SecureRandom();

	public static String generateNumericOTP(int length) {
		StringBuilder otp = new StringBuilder();
		for (int i = 0; i < length; i++) {
			otp.append(random.nextInt(10));
		}
		return otp.toString();
	}
	
}
