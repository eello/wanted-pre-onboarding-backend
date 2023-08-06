package jongseongkim.wantedpreonboardingbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserLoginRequestVO {

	private String email;
	private String password;
}
