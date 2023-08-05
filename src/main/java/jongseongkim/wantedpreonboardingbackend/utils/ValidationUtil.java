package jongseongkim.wantedpreonboardingbackend.utils;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;

public class ValidationUtil {

	/**
	 * 이메일에 @ 포함 검사
	 * @param email
	 */
	public static void validateEmail(String email) {
		if (email.split("@").length <= 1) {
			throw new IllegalArgumentException(INVALID_EMAIL_FORMAT.getDescription());
		}
	}

	/**
	 * 비밀번호 8자 이상 검사
	 * @param password
	 */
	public static void validatePassword(String password) {
		if (password.length() < 8) {
			throw new IllegalArgumentException(INVALID_PASSWORD_FORMAT.getDescription());
		}
	}
}
