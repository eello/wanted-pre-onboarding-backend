package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.exception.ErrorDescription.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import jongseongkim.wantedpreonboardingbackend.dto.BoardDTO;
import jongseongkim.wantedpreonboardingbackend.dto.PaginationDTO;
import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.exception.NotAuthenticated;
import jongseongkim.wantedpreonboardingbackend.repository.BoardRepository;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import jongseongkim.wantedpreonboardingbackend.vo.BoardRegisterRequestVO;

@ExtendWith(MockitoExtension.class)
class BoardServiceImplTest {

	@Mock
	private BoardRepository boardRepository;

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private BoardServiceImpl boardService;

	@Test
	public void 등록_Arg_null_실패_vo_is_null() {
	    // given
	    // when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(null, "aa@aa.a"));

	    // then
		assertEquals(ARG_IS_NULL.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_Arg_null_실패_writerEmail_is_null() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, null));

	    // then
		assertEquals(ARG_IS_NULL.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_제목_유효성_실패_is_null() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title(null)
			.content("content")
			.build();
		String writerEmail = "aa@aa.a";

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

	    // then
		assertEquals(INVALID_TITLE.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_제목_유효성_실패_length_is_0() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("")
			.content("content")
			.build();
		String writerEmail = "aa@aa.a";

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

	    // then
		assertEquals(INVALID_TITLE.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_제목_유효성_실패_is_whitespace() {
		// given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title(" ")
			.content("content")
			.build();
		String writerEmail = "aa@aa.a";

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

		// then
		assertEquals(INVALID_TITLE.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_본문_유효성_실패_is_null() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content(null)
			.build();
		String writerEmail = "aa@aa.a";

	    // when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

	    // then
	    assertEquals(INVALID_CONTENT.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_본문_유효성_실패_length_is_0() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("")
			.build();
		String writerEmail = "aa@aa.a";

	    // when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

	    // then
	    assertEquals(INVALID_CONTENT.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_본문_유효성_실패_is_whitespace() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content(" ")
			.build();
		String writerEmail = "aa@aa.a";

	    // when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.register(vo, writerEmail));

	    // then
	    assertEquals(INVALID_CONTENT.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_실패_존재하지_않는_작성자() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();
		String writerEmail = "aa@aa.a";

		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

	    // when
		EntityNotFoundException exception =
			assertThrows(EntityNotFoundException.class, () -> boardService.register(vo, writerEmail));

	    // then
	    assertEquals(NOT_FOUND_USER.getDescription(), exception.getMessage());
	}

	@Test
	public void 등록_성공() {
	    // given
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

		User writer = User.builder()
			.email("aa@aa.a")
			.build();

		long boardId = 1L;
		Board board = Board.builder()
			.id(boardId)
			.writer(writer)
			.title(vo.getTitle())
			.content(vo.getContent())
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(writer));
		given(boardRepository.save(any(Board.class))).willReturn(board);

		// when
		Long registeredBoardId = assertDoesNotThrow(() -> boardService.register(vo, writer.getEmail()));

		// then
		assertEquals(boardId, registeredBoardId);
	}

	@Test
	public void 게시글_목록_조회_empty() {
	    // given
		int page = 0;
		int size = 20;
		PageRequest pageable = PageRequest.of(page, size);
		given(boardRepository.findAll(any(PageRequest.class)))
			.willReturn(new PageImpl<Board>(Collections.emptyList(), pageable, 0));

	    // when
		PaginationDTO<Board, BoardDTO> result =
			assertDoesNotThrow(() -> boardService.getBoardsWithPaging(pageable));

		// then
		assertTrue(result.getIsFirst());
		assertTrue(result.getIsLast());
		assertTrue(result.getIsEmpty());
		assertFalse(result.getHasPrevious());
		assertFalse(result.getHasNext());
		assertEquals(page, result.getPageNum());
		assertEquals(size, result.getSizePerPage());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getTotalPages());
		assertTrue(result.getContent().isEmpty());
	}

	@Test
	public void 게시글_목록_조회() {
	    // given
		int totalElements = 100;
		List<Board> contents = new ArrayList<>();
		for (int i = 0; i < totalElements; i++) {
			User writer = User.builder()
				.id((long)i)
				.email(i + "aa@aa.a")
				.build();

			Board board = Board.builder()
				.id((long)i)
				.writer(writer)
				.title("title" + i)
				.build();

			contents.add(board);
		}

		int page = 0;
		int size = 20;
		PageRequest pageable = PageRequest.of(page, size);
		given(boardRepository.findAll(pageable))
			.willReturn(new PageImpl<>(contents, pageable, contents.size()));

	    // when
		PaginationDTO<Board, BoardDTO> result =
			assertDoesNotThrow(() -> boardService.getBoardsWithPaging(pageable));

		// then
		List<BoardDTO> expectedContent = contents.stream()
			.map(BoardDTO::of)
			.collect(Collectors.toList());
		int expectedTotalPages = (int)Math.ceil((double)totalElements / (double)size);

		assertTrue(result.getIsFirst());
		assertFalse(result.getIsLast());
		assertFalse(result.getIsEmpty());
		assertFalse(result.getHasPrevious());
		assertTrue(result.getHasNext());
		assertEquals(page, result.getPageNum());
		assertEquals(size, result.getSizePerPage());
		assertEquals(totalElements, result.getTotalElements());
		assertEquals(expectedTotalPages, result.getTotalPages());
		assertIterableEquals(expectedContent, result.getContent());
	}

	@Test
	public void 특정_게시글_조회_실패_not_found() {
	    // given
		given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

	    // when
		EntityNotFoundException exception =
			assertThrows(EntityNotFoundException.class, () -> boardService.getById(1L));

	    // then
		assertEquals(NOT_FOUND_BOARD.getDescription(), exception.getMessage());
	}

	@Test
	public void 특정_게시글_조회_성공() {
		// given
		User writer = User.builder()
			.id(1L)
			.email("aa@aa.a")
			.build();

		Board board = Board.builder()
			.writer(writer)
			.id(1L)
			.title("title")
			.content("content")
			.build();

		given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

		// when
		BoardDTO result = assertDoesNotThrow(() -> boardService.getById(board.getId()));

		// then
		assertEquals(writer.getId(), result.getWriter().getId());
		assertEquals(writer.getEmail(), result.getWriter().getEmail());
		assertEquals(board.getId(), result.getId());
		assertEquals(board.getTitle(), result.getTitle());
		assertEquals(board.getContent(), result.getContent());
	}

	@Test
	public void 게시글_수정_실패_writerEmail_is_null() {
	    // given
	 	Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

	    // when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.update(null, boardId, vo));

		// then
	    assertEquals(ARG_IS_NULL.getDescription(), exception.getMessage());
	}

	@Test
	public void 게시글_수정_실패_boardId_is_null() {
		// given
		String writerEmail = "aa@aa.a";
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.update(writerEmail, null, vo));

		// then
		assertEquals(ARG_IS_NULL.getDescription(), exception.getMessage());
	}

	@Test
	public void 게시글_수정_실패_vo_is_null() {
		// given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;

		// when
		IllegalArgumentException exception =
			assertThrows(IllegalArgumentException.class, () -> boardService.update(writerEmail, boardId, null));

		// then
		assertEquals(ARG_IS_NULL.getDescription(), exception.getMessage());
	}

	@Test
	public void 게시글_수정_실패_writer_not_found() {
	    // given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

	    // when
		NotAuthenticated exception =
			assertThrows(NotAuthenticated.class, () -> boardService.update(writerEmail, boardId, vo));

		// then
		assertEquals(NOT_AUTHENTICATED.getDescription(), exception.getMessage());
	}

	@Test
	public void 게시글_수정_실패_board_not_found() {
	    // given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title")
			.content("content")
			.build();

		User writer = User.builder()
			.id(1L)
			.email(writerEmail)
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(writer));
		given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

	    // when
		EntityNotFoundException exception =
			assertThrows(EntityNotFoundException.class, () -> boardService.update(writerEmail, boardId, vo));

		// then
	    assertEquals(NOT_FOUND_BOARD.getDescription(), exception.getMessage());
	}

	@Test
	public void 게시글_수정_성공_제목만() {
	    // given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title after update")
			.content(null)
			.build();

		User writer = User.builder()
			.id(1L)
			.email(writerEmail)
			.build();

		Board board = Board.builder()
			.writer(writer)
			.title("title before update")
			.content("content before update")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(writer));
		given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

		// when
		assertDoesNotThrow(() -> boardService.update(writerEmail, boardId, vo));

	    // then
		assertEquals(vo.getTitle(), board.getTitle());
		assertNotNull(board.getContent());
		assertEquals("content before update", board.getContent());
	}

	@Test
	public void 게시글_수정_성공_본문만() {
		// given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title(null)
			.content("content after update")
			.build();

		User writer = User.builder()
			.id(1L)
			.email(writerEmail)
			.build();

		Board board = Board.builder()
			.writer(writer)
			.title("title before update")
			.content("content before update")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(writer));
		given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

		// when
		assertDoesNotThrow(() -> boardService.update(writerEmail, boardId, vo));

		// then
		assertNotNull(board.getTitle());
		assertEquals("title before update", board.getTitle());
		assertEquals(vo.getContent(), board.getContent());
	}

	@Test
	public void 게시글_수정_성공_전부() {
		// given
		String writerEmail = "aa@aa.a";
		Long boardId = 1L;
		BoardRegisterRequestVO vo = BoardRegisterRequestVO.builder()
			.title("title after update")
			.content("content after update")
			.build();

		User writer = User.builder()
			.id(1L)
			.email(writerEmail)
			.build();

		Board board = Board.builder()
			.writer(writer)
			.title("title before update")
			.content("content before update")
			.build();

		given(userRepository.findByEmail(anyString())).willReturn(Optional.of(writer));
		given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));

		// when
		assertDoesNotThrow(() -> boardService.update(writerEmail, boardId, vo));

		// then
		assertEquals(vo.getTitle(), board.getTitle());
		assertEquals(vo.getContent(), board.getContent());
	}
}