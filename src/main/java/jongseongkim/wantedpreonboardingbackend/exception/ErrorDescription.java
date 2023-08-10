package jongseongkim.wantedpreonboardingbackend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorDescription {

	INVALID_EMAIL_FORMAT("이메일은 '@'를 반드시 포함해야합니다."),
	INVALID_PASSWORD_FORMAT("비밀번호는 8자 이상이어야합니다."),
	ALREADY_EXISTS_EMAIL("이미 존재하는 이메일입니다."),
	LOGIN_FAIL("이메일과 비밀번호를 다시 확인해주세요."),
	INVALID_TITLE("제목은 필수이며 최소 공백이 아닌 한 글자를 포함해야합니다."),
	INVALID_CONTENT("본문은 필수이며 최소 공백이 아닌 한 글자를 포함해야합니다."),
	NOT_FOUND_USER("존재하지 않는 유저입니다."),
	ARG_IS_NULL("값이 Null인 Argument가 존재합니다."),
	NOT_FOUND_BOARD("존재하지 않는 게시글입니다."),
	NOT_AUTHENTICATED("인증이 필요한 서비스입니다."),
	NOT_AUTHORIZED("권한이 없습니다."),

	;

	private final String description;
}
