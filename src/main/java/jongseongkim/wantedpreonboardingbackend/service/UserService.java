package jongseongkim.wantedpreonboardingbackend.service;

import javax.security.auth.login.LoginException;

import jongseongkim.wantedpreonboardingbackend.dto.TokenDTO;
import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;
import jongseongkim.wantedpreonboardingbackend.vo.UserLoginRequestVO;

public interface UserService {

	Long join(UserJoinRequestVO vo);
	TokenDTO login(UserLoginRequestVO vo) throws LoginException;
}
