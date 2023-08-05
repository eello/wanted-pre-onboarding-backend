package jongseongkim.wantedpreonboardingbackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jongseongkim.wantedpreonboardingbackend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByEmail(String email);
}
