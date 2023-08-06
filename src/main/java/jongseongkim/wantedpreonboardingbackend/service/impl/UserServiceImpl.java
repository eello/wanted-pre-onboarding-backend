package jongseongkim.wantedpreonboardingbackend.service.impl;

import javax.persistence.EntityExistsException;
import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jongseongkim.wantedpreonboardingbackend.dto.TokenDTO;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.error.ErrorDescription;
import jongseongkim.wantedpreonboardingbackend.jwt.JwtProvider;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.service.UserService;
import jongseongkim.wantedpreonboardingbackend.utils.ValidationUtil;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import jongseongkim.wantedpreonboardingbackend.vo.UserLoginRequestVO;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;

	@Override
	@Transactional
	public Long join(UserJoinRequestVO vo) {
		User user = vo.toEntity();

		// 이미 존재하는 이메일인 경우
		if (userRepository.existsByEmail(user.getEmail())) {
			throw new EntityExistsException(ErrorDescription.ALREADY_EXISTS_EMAIL.getDescription());
		}

		return userRepository.save(user).getId();
	}

	@Override
	@Transactional
	public TokenDTO login(UserLoginRequestVO vo) throws LoginException {
		// 이메일, 비밀번호 유효성 검사
		ValidationUtil.validateEmail(vo.getEmail());
		ValidationUtil.validatePassword(vo.getPassword());

		// 이메일에 해당하는 유저가 존재하지 않음
		User user = userRepository.findByEmail(vo.getEmail())
			.orElseThrow(() -> new LoginException(ErrorDescription.LOGIN_FAIL.getDescription()));

		if (!user.login(vo.getPassword())) { // 비밀번호가 일치하지 않음
			throw new LoginException(ErrorDescription.LOGIN_FAIL.getDescription());
		}

		String accessToken = jwtProvider.generateAccessToken(user);
		String refreshToken = jwtProvider.generateRefreshToken(user);

		user.setRefreshToken(refreshToken);

		return TokenDTO.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}
