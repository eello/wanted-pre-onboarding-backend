package jongseongkim.wantedpreonboardingbackend.conroller;

import static jongseongkim.wantedpreonboardingbackend.jwt.UserExtractionFromJwtResolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import jongseongkim.wantedpreonboardingbackend.dto.BoardDTO;
import jongseongkim.wantedpreonboardingbackend.dto.PaginationDTO;
import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.service.BoardService;
import jongseongkim.wantedpreonboardingbackend.vo.BoardRegisterRequestVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

	private final BoardService boardService;

	@PostMapping
	public void register(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestBody BoardRegisterRequestVO vo, ExtractedUser eu) {
		Long registeredBoardId = boardService.register(vo, eu.getEmail());

		String scheme = request.getScheme();
		String host = request.getHeader("Host");
		String path = "/boards/" + registeredBoardId;

		UriComponents resourceUri = UriComponentsBuilder.newInstance()
			.scheme(scheme)
			.host(host)
			.path(path)
			.build();

		response.setHeader(HttpHeaders.LOCATION, resourceUri.toString());
		response.setStatus(HttpServletResponse.SC_CREATED);
	}

	@GetMapping
	public PaginationDTO<Board, BoardDTO> getBoards(Pageable pageable) {
		PaginationDTO<Board, BoardDTO> result = boardService.getBoardsWithPaging(pageable);
		return result;
	}

	@GetMapping("/{id}")
	public BoardDTO getBoardById(@PathVariable("id") Long boardId) {
		return boardService.getById(boardId);
	}

	@PatchMapping("/{id}")
	public void update(
		HttpServletResponse response,
		@PathVariable("id") Long boardId,
		@RequestBody BoardRegisterRequestVO vo,
		ExtractedUser eu) {
		boardService.update(eu.getEmail(), boardId, vo);
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
}
