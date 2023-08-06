package jongseongkim.wantedpreonboardingbackend.conroller;

import javax.persistence.EntityExistsException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@RestControllerAdvice
public class GlobalControllerAdvisor {

	@ExceptionHandler(value = {IllegalArgumentException.class, LoginException.class})
	public ErrorResponse handleBadRequestException(
		Exception e, HttpServletResponse response) {
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		return ErrorResponse.of(e);
	}

	@ExceptionHandler(value = {EntityExistsException.class})
	public ErrorResponse handleConflictException(
		Exception e, HttpServletResponse response) {
		response.setStatus(HttpStatus.CONFLICT.value());
		return ErrorResponse.of(e);
	}

	@NoArgsConstructor
	@AllArgsConstructor
	@Getter
	public static class ErrorResponse {

		String errorDescription;

		public static ErrorResponse of(Exception e) {
			return new ErrorResponse(e.getMessage());
		}
	}
}
