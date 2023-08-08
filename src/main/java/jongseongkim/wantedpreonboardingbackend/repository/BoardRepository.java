package jongseongkim.wantedpreonboardingbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jongseongkim.wantedpreonboardingbackend.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
