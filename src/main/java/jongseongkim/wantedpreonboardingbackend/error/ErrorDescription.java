package jongseongkim.wantedpreonboardingbackend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorDescription {

	INVALID_EMAIL_FORMAT("이메일은 '@'를 반드시 포함해야합니다."),
	INVALID_PASSWORD_FORMAT("비밀번호는 8자 이상이어야합니다."),
	ALREADY_EXISTS_EMAIL("이미 존재하는 이메일입니다."),
	LOGIN_FAIL("이메일과 비밀번호를 다시 확인해주세요."),
	;

	private final String description;
}
