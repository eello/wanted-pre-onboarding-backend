package jongseongkim.wantedpreonboardingbackend.exception;

public class NotAuthorized extends RuntimeException {

	public NotAuthorized() {
	}

	public NotAuthorized(String message) {
		super(message);
	}

	public NotAuthorized(String message, Throwable cause) {
		super(message, cause);
	}

	public NotAuthorized(Throwable cause) {
		super(cause);
	}

	public NotAuthorized(String message, Throwable cause, boolean enableSuppression,
		boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
