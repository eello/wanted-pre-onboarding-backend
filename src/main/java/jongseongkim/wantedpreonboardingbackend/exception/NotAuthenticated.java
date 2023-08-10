package jongseongkim.wantedpreonboardingbackend.exception;

/**
 * 인증이 되지 않은 사용자로 부터의 요청인경우 throw
 * ex) 토큰에
 */
public class NotAuthenticated extends RuntimeException {

	public NotAuthenticated() {
	}

	public NotAuthenticated(String message) {
		super(message);
	}

	public NotAuthenticated(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAuthenticated(Throwable cause) {
		super(cause);
	}

	public NotAuthenticated(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
