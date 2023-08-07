package jongseongkim.wantedpreonboardingbackend.jwt;

import static jongseongkim.wantedpreonboardingbackend.conroller.GlobalControllerAdvisor.*;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private static final ObjectMapper OM = new ObjectMapper();

	public static final String AUTH_HEADER_NAME = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";

	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		if ("GET".equals(request.getMethod())) {
			filterChain.doFilter(request, response);
			return;
		}

		System.out.println("run jwt filter");

		String authorization = request.getHeader(AUTH_HEADER_NAME);

		// 토큰은 텍스트가 존재해야하고 'Bearer '로 시작하는 문자열이어야 한다.
		if (StringUtils.hasText(authorization) && authorization.startsWith(TOKEN_PREFIX)) {
			String jwt = authorization.substring(7);

			try {
				jwtProvider.validateToken(jwt);
				doFilter(request, response, filterChain);
			} catch (ExpiredJwtException e) {
				// 만료된 토큰 예외
				handleUnauthorized(response, "만료된 토큰입니다.");
			} catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
				// 유효하지 않는 토큰 예외
				handleUnauthorized(response, "유효하지 않은 토큰입니다.");
			}
		} else {
			// 토큰이 포함되지 않았거나 토큰 타입이 일치하지 않음
			handleUnauthorized(response, "토큰이 포함되지 않거나 지원하지 않는 토큰 타입입니다.");
		}
	}

	/**
	 * 401 응답과 ErrorResponse를 포함해 response를 설정
	 */
	private static void handleUnauthorized(HttpServletResponse httpResponse, String errorMessage) throws IOException {
		ErrorResponse errorResponse = new ErrorResponse(errorMessage);
		httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		httpResponse.setContentType("application/json");
		httpResponse.setCharacterEncoding("utf8");
		httpResponse.getWriter().write(OM.writeValueAsString(errorResponse));
	}
}
