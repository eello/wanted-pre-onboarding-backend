package jongseongkim.wantedpreonboardingbackend.entity;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jongseongkim.wantedpreonboardingbackend.error.ErrorDescription;

@DataJpaTest
class UserTest {

	@Autowired
	private EntityManager em;

	@Test
	public void 유저_이메일_유효성_실패() {
		String email = "aaa";
		String password = "password";

		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> User.builder()
				.email(email)
				.password(password)
				.build());

		assertEquals(INVALID_EMAIL_FORMAT.getDescription(), exception.getMessage());
	}
	@Test
	public void 유저_비밀번호_유효성_실패() {
		String email = "aa@aa.a";
		String password = "passwor";

		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> User.builder()
				.email(email)
				.password(password)
				.build());

		assertEquals(INVALID_PASSWORD_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 유저_DB_저장() {
		String email = "aa@aa.a";
		String password = "password";

		User user = assertDoesNotThrow(() -> User.builder()
			.email(email)
			.password(password)
			.build());

		em.persist(user);
		em.flush();
		em.clear();

		User findUser = em.find(User.class, user.getId());
		assertEquals(user.getId(), findUser.getId());
	}
}