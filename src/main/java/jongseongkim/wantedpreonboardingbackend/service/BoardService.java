package jongseongkim.wantedpreonboardingbackend.service;

import jongseongkim.wantedpreonboardingbackend.vo.BoardRegisterRequestVO;

public interface BoardService {

	Long register(BoardRegisterRequestVO vo, String writerEmail);
}
