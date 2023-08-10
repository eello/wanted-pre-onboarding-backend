package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.exception.ErrorDescription.*;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import jongseongkim.wantedpreonboardingbackend.dto.BoardDTO;
import jongseongkim.wantedpreonboardingbackend.dto.PaginationDTO;
import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.exception.NotAuthenticated;
import jongseongkim.wantedpreonboardingbackend.exception.NotAuthorized;
import jongseongkim.wantedpreonboardingbackend.repository.BoardRepository;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.service.BoardService;
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

	@Override
	public PaginationDTO<Board, BoardDTO> getBoardsWithPaging(Pageable pageable) {
		Page<Board> boards = boardRepository.findAll(pageable);
		return new PaginationDTO<>(boards, BoardDTO.mapper());
	}

	@Override
	public BoardDTO getById(Long boardID) {
		Assert.notNull(boardID, ARG_IS_NULL.getDescription());

		Board board = boardRepository.findById(boardID)
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_BOARD.getDescription()));

		return BoardDTO.of(board);
	}

	@Override
	@Transactional
	public void update(String writerEmail, Long boardId, BoardRegisterRequestVO vo) {
		Assert.notNull(writerEmail, ARG_IS_NULL.getDescription());
		Assert.notNull(boardId, ARG_IS_NULL.getDescription());
		Assert.notNull(vo, ARG_IS_NULL.getDescription());

		User writer = userRepository.findByEmail(writerEmail)
			.orElseThrow(() -> new NotAuthenticated(NOT_AUTHENTICATED.getDescription()));

		Board board = boardRepository.findById(boardId)
			.orElseThrow(() -> new EntityNotFoundException(NOT_FOUND_BOARD.getDescription()));

		if (!board.isWrittenBy(writer)) {
			throw new NotAuthorized(NOT_AUTHORIZED.getDescription());
		}

		if (StringUtils.hasText(vo.getTitle())) {
			board.updateTitle(vo.getTitle());
		}

		if (StringUtils.hasText(vo.getContent())) {
			board.updateContent(vo.getContent());
		}
	}
}
