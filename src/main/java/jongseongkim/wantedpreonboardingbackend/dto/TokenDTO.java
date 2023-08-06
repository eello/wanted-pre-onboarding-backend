package jongseongkim.wantedpreonboardingbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenDTO {

	private String accessToken;
	private String refreshToken;
}
