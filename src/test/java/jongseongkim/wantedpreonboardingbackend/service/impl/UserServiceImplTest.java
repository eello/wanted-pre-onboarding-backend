package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import javax.persistence.EntityExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userService;

	@Test
	public void 회원가입_이메일_유효성_실패() {
		UserJoinRequestVO vo = UserJoinRequestVO.builder()
			.email("aaaa")
			.password("password")
			.build();

		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> userService.join(vo));

		assertEquals(INVALID_EMAIL_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 회원가입_비밀번호_유효성_실패() {
		UserJoinRequestVO vo = UserJoinRequestVO.builder()
			.email("aa@aa.a")
			.password("passwor")
			.build();

		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> userService.join(vo));

		assertEquals(INVALID_PASSWORD_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 회원가입_이메일_중복() {
		UserJoinRequestVO vo = UserJoinRequestVO.builder()
			.email("aa@aa.a")
			.password("password")
			.build();

		given(userRepository.existsByEmail(anyString())).willReturn(true);

		EntityExistsException exception =
			assertThrows(EntityExistsException.class, () -> userService.join(vo));

		assertEquals(ALREADY_EXISTS_EMAIL.getDescription(), exception.getMessage());
	}

	@Test
	public void 회원가입_성공() {
		String email = "aa@aa.a";
		String password = "password";
		UserJoinRequestVO vo = UserJoinRequestVO.builder()
			.email(email)
			.password(password)
			.build();

		User user = User.builder()
			.id(1L)
			.email(email)
			.password(password)
			.build();

		given(userRepository.existsByEmail(anyString())).willReturn(false);
		given(userRepository.save(any(User.class))).willReturn(user);

		Long savedUserId = assertDoesNotThrow(() -> userService.join(vo));
		assertEquals(savedUserId, user.getId());
	}
}