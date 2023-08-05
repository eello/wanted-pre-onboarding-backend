package jongseongkim.wantedpreonboardingbackend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.util.Assert;

import jongseongkim.wantedpreonboardingbackend.utils.ValidationUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users", uniqueConstraints = {
	@UniqueConstraint(name = "UniqueEmail", columnNames = {"email"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@ToString
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	private String refreshToken;

	public static class UserBuilder {

		/**
		 * 이메일 형식을 검사하는 빌더 패턴의 커스텀 함수
		 * @param email
		 */
		public UserBuilder email(String email) {
			ValidationUtil.validateEmail(email);
			this.email = email;

			return this;
		}

		/**
		 * 비밀번호 형식을 검사하는 빌더 패턴의 커스텀 함수
		 * @param password
		 */
		public UserBuilder password(String password) {
			ValidationUtil.validatePassword(password);
			this.password = password;

			return this;
		}
	}
}
