package jongseongkim.wantedpreonboardingbackend.conroller;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jongseongkim.wantedpreonboardingbackend.dto.TokenDTO;
import jongseongkim.wantedpreonboardingbackend.service.UserService;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import jongseongkim.wantedpreonboardingbackend.vo.UserLoginRequestVO;
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

	@PostMapping("/login")
	public Map<String, String> login(HttpServletResponse response, @RequestBody UserLoginRequestVO vo) throws LoginException {
		TokenDTO token = userService.login(vo);

		// HttpOnly Refresh Token Cookie 생성
		Cookie refreshTokenCookie = new Cookie("WPOBRefreshToken", token.getRefreshToken());
		refreshTokenCookie.setHttpOnly(true);
		response.addCookie(refreshTokenCookie);

		Map<String, String> res = new HashMap<>();
		res.put("accessToken", token.getAccessToken());
		return res;
	}
}
