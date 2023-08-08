package jongseongkim.wantedpreonboardingbackend.service.impl;

import static jongseongkim.wantedpreonboardingbackend.error.ErrorDescription.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.entity.User;
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
}