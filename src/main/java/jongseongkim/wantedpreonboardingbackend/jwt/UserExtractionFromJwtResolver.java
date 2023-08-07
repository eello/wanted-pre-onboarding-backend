package jongseongkim.wantedpreonboardingbackend.jwt;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jongseongkim.wantedpreonboardingbackend.entity.User;
import jongseongkim.wantedpreonboardingbackend.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 인증이 필요한 엔드포인트에 인자로 ExtractedUser 타입을 포함하면
 * Http Header의 Authorization에 포함된 jwt의 subject(user.email)을 이용해
 * 요청한 유저 정보를 추출하는 HandlerMethodArgumentResolver
 */
@Component
@RequiredArgsConstructor
public class UserExtractionFromJwtResolver implements HandlerMethodArgumentResolver {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterType().equals(ExtractedUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

		ExtractedUser eu = new ExtractedUser();

		String authorization = webRequest.getHeader(JwtFilter.AUTH_HEADER_NAME);
		if (StringUtils.hasText(authorization) && authorization.startsWith(JwtFilter.TOKEN_PREFIX)) {
			String jwt = authorization.substring(7);

			String userEmail = jwtProvider.parseClaims(jwt).getSubject();
			userRepository.findByEmail(userEmail).ifPresent(eu::setUser);
		}

		return eu;
	}

	@Getter
	@ToString
	public static class ExtractedUser {

		private boolean isExists;
		private Long id;
		private String email;

		public void setUser(User user) {
			isExists = true;
			id = user.getId();
			email = user.getEmail();
		}
	}
}
