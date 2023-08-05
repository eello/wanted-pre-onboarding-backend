package jongseongkim.wantedpreonboardingbackend.conroller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jongseongkim.wantedpreonboardingbackend.service.UserService;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping
	public void join(HttpServletResponse response, @RequestBody UserJoinRequestVO vo) {
		userService.join(vo);
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
}
