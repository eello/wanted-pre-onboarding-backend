package jongseongkim.wantedpreonboardingbackend.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jongseongkim.wantedpreonboardingbackend.jwt.JwtFilter;
import jongseongkim.wantedpreonboardingbackend.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomFilterConfig {

	private final JwtProvider jwtProvider;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter() {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter(jwtProvider));

		// 필터를 적용할 url pattern 등록
		// registrationBean.addUrlPatterns("");

		return registrationBean;
	}
}
