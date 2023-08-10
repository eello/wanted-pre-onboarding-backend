package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.exception.ErrorDescription.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.security.auth.login.LoginException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import jongseongkim.wantedpreonboardingbackend.dto.TokenDTO;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.jwt.JwtProvider;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import jongseongkim.wantedpreonboardingbackend.vo.UserLoginRequestVO;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	private final String jwtSecret = "7qA6g3HeM8wBxqSKUQrHO3tQ3q9n1jazW5hGEqloBIMxKm2XzbBmf8OVWRZMn4SLdSd5MB96";

	@Mock
	private UserRepository userRepository;

	@Spy
	private JwtProvider jwtProvider = new JwtProvider(jwtSecret);

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

	@Test
	public void 로그인_이메일_유효성_실패() {
		// given
		UserLoginRequestVO vo = UserLoginRequestVO.builder()
			.email("aaaa")
			.password("password")
			.build();

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> userService.login(vo));

		// then
		assertEquals(INVALID_EMAIL_FORMAT.getDescription(), exception.getMessage());
	}

	@Test
	public void 로그인_비밀번호_유효성_실패() {
	    // given
		UserLoginRequestVO vo = UserLoginRequestVO.builder()
			.email("aa@aa.a")
			.password("passwor")
			.build();

	    // when
		IllegalArgumentException exception = 
			assertThrows(IllegalArgumentException.class, () -> userService.login(vo));

		// then
		assertEquals(INVALID_PASSWORD_FORMAT.getDescription(), exception.getMessage());
	}
	
	@Test
	public void 존재하지_않는_이메일() {
	    // given
		UserLoginRequestVO vo = UserLoginRequestVO.builder()
			.email("aa@aa.a")
			.password("password")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());
	    
	    // when
		LoginException exception = assertThrows(LoginException.class, () -> userService.login(vo));

		// then
		assertEquals(LOGIN_FAIL.getDescription(), exception.getMessage());
	}

	@Test
	public void 비밀번호가_일치하지_않음() {
	    // given
		String email = "aa@aa.a";
		String password = "password";
		UserLoginRequestVO vo = UserLoginRequestVO.builder()
			.email(email)
			.password(password)
			.build();

		User user = User.builder()
			.email(email)
			.password("differentPassword")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

		// when
		LoginException exception = assertThrows(LoginException.class, () -> userService.login(vo));

		// then
		assertEquals(LOGIN_FAIL.getDescription(), exception.getMessage());
	}

	@Test
	public void 로그인_성공() {
	    // given
		String email = "aa@aa.a";
		String password = "password";
		UserLoginRequestVO vo = UserLoginRequestVO.builder()
			.email(email)
			.password(password)
			.build();

		User user = User.builder()
			.email(email)
			.password(password)
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

	    // when
		TokenDTO token = assertDoesNotThrow(() -> userService.login(vo));

		// then
	    assertNotNull(user.getRefreshToken()); // user에 리프레시 토큰 저장
		assertNotNull(token); // 리턴한 tokenDTO not null
		assertNotNull(token.getAccessToken()); // accessToken 반환
		assertNotNull(token.getRefreshToken()); // refreshToken 반환
		assertEquals(token.getRefreshToken(), user.getRefreshToken()); // 반환된 refresh token과 유저에 저장된 refresh token 일치
	}
}