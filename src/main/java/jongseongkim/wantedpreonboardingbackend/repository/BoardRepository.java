package jongseongkim.wantedpreonboardingbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import jongseongkim.wantedpreonboardingbackend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {

	@EntityGraph(attributePaths = {"writer"})
	Page<Board> findAll(Pageable pageable);
}
