package com.Bruno.LifeHub.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import com.Bruno.LifeHub.entities.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

	@Autowired
	private UserRepository repository;

	@Test
	void findByEmailDeveRetornarUsuarioQuandoEmailValido() {

		User user = new User();
		user.setName("Bruno");
		user.setEmail("bruno@email.com");
		user.setPassword("123456");

		repository.save(user);

		Optional<User> result = repository.findByEmail("bruno@email.com");

		assertTrue(result.isPresent());
		assertEquals("Bruno", result.get().getName());
		assertEquals("bruno@email.com", result.get().getEmail());
	}

	@Test
	void findByEmailDeveRetornarOptionalVazioQuandoEmailNaoExiste() {

		Optional<User> result = repository.findByEmail("naoexiste@email.com");

		assertTrue(result.isEmpty());
	}

	@Test
	void existsByEmailDeveRetornarTrueQuandoEmailExiste() {

		User user = new User();
		user.setName("Bruno");
		user.setEmail("bruno@email.com");
		user.setPassword("123456");

		repository.save(user);

		boolean result = repository.existsByEmail("bruno@email.com");

		assertTrue(result);
	}

	@Test
	void existsByEmailDeveRetornarFalseQuandoEmailNaoExiste() {

		boolean result = repository.existsByEmail("naoexiste@email.com");

		assertFalse(result);
	}
}