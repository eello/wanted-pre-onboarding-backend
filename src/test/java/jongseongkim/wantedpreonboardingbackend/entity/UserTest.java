package jongseongkim.wantedpreonboardingbackend.entity;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserTest {

	@Autowired
	private EntityManager em;

	@Test
	public void 유저_생성() {
		User user = User.builder()
			.email("aa@aa.a")
			.password("pwd")
			.build();

		em.persist(user);
		em.flush();
		em.clear();

		User findUser = em.find(User.class, user.getId());

		assertThat(user.getId()).isEqualTo(findUser.getId());
	}
}