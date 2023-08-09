package jongseongkim.wantedpreonboardingbackend.service;

import org.springframework.data.domain.Pageable;

import jongseongkim.wantedpreonboardingbackend.dto.BoardDTO;
import jongseongkim.wantedpreonboardingbackend.dto.PaginationDTO;
import jongseongkim.wantedpreonboardingbackend.entity.Board;
import jongseongkim.wantedpreonboardingbackend.vo.BoardRegisterRequestVO;

public interface BoardService {

	Long register(BoardRegisterRequestVO vo, String writerEmail);

	PaginationDTO<Board, BoardDTO> getBoardsWithPaging(Pageable pageable);
}
