package jongseongkim.wantedpreonboardingbackend.utils;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jongseongkim.wantedpreonboardingbackend.error.ErrorDescription;

class ValidationUtilTest {

	@Test
	public void 이메일_골뱅이_미포함() {
		String email = "email";
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateEmail(email));

		assertEquals(INVALID_EMAIL_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 이메일_골뱅이_포함() {
		String email = "email@email.com";
		assertDoesNotThrow(() -> ValidationUtil.validateEmail(email));
	}

	@Test
	public void 비밀번호_8자_미만() {
		String password = "passwor";
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateEmail(password));

		assertEquals(INVALID_EMAIL_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 비밀번호_8자_이상() {
		String password = "password";
		assertDoesNotThrow(() -> ValidationUtil.validatePassword(password));
	}
}