package jongseongkim.wantedpreonboardingbackend.config;

import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import jongseongkim.wantedpreonboardingbackend.jwt.JwtFilter;
import jongseongkim.wantedpreonboardingbackend.jwt.JwtProvider;
import jongseongkim.wantedpreonboardingbackend.jwt.UserExtractionFromJwtResolver;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfiguration implements WebMvcConfigurer {

	private final UserExtractionFromJwtResolver userExtractionFromJwtResolver;

	@Bean
	public FilterRegistrationBean<JwtFilter> jwtFilter(JwtProvider jwtProvider) {
		FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new JwtFilter(jwtProvider));

		// 필터를 적용할 url pattern 등록
		registrationBean.addUrlPatterns("/boards", "/boards/*");

		return registrationBean;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userExtractionFromJwtResolver);
	}
}
