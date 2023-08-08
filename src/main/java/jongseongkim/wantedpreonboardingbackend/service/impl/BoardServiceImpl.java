package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.error.ErrorDescription;
import jongseongkim.wantedpreonboardingbackend.repository.BoardRepository;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.service.BoardService;
import jongseongkim.wantedpreonboardingbackend.utils.ValidationUtil;
import jongseongkim.wantedpreonboardingbackend.vo.BoardRegisterRequestVO;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public Long register(BoardRegisterRequestVO vo, String writerEmail) {
		// argument null 검사
		Assert.notNull(vo, ARG_IS_NULL.getDescription());
		Assert.notNull(writerEmail, ARG_IS_NULL.getDescription());

		// 제목과 본문 유효성 검사: not null && 공백이 아닌 최소 1글자 포함
		Assert.hasText(vo.getTitle(), INVALID_TITLE.getDescription());
		Assert.hasText(vo.getContent(), INVALID_CONTENT.getDescription());

		User writer = userRepository.findByEmail(writerEmail)
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_USER.getDescription()));

		Board board = Board.builder()
			.writer(writer)
			.title(vo.getTitle())
			.content(vo.getContent())
			.build();

		return boardRepository.save(board).getId();
	}
}
