package jongseongkim.wantedpreonboardingbackend.conroller;

import static jongseongkim.wantedpreonboardingbackend.jwt.UserExtractionFromJwtResolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

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


}
