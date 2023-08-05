package jongseongkim.wantedpreonboardingbackend.vo;

import jongseongkim.wantedpreonboardingbackend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserJoinRequestVO {

	private String email;
	private String password;

	public User toEntity() {
		return User.builder()
			.email(email)
			.password(password)
			.build();
	}
}
