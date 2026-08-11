package com.Bruno.LifeHub.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Bruno.LifeHub.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
	boolean existsByEmail(String email);
}
