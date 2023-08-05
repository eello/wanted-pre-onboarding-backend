package jongseongkim.wantedpreonboardingbackend.service.impl;

import javax.persistence.EntityExistsException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.error.ErrorDescription;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.service.UserService;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

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
}
