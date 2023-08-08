package jongseongkim.wantedpreonboardingbackend.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRegisterRequestVO {

	private String title;
	private String content;
}
