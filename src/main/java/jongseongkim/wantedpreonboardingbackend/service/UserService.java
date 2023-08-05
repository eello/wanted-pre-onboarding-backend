package jongseongkim.wantedpreonboardingbackend.service;

import jongseongkim.wantedpreonboardingbackend.vo.UserJoinRequestVO;

public interface UserService {

	Long join(UserJoinRequestVO vo);
}
