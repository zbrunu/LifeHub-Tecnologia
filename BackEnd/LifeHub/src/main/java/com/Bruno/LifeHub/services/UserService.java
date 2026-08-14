package com.Bruno.LifeHub.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Bruno.LifeHub.dto.UserDTO;
import com.Bruno.LifeHub.dto.UserInsertDTO;
import com.Bruno.LifeHub.entities.User;
import com.Bruno.LifeHub.repositories.UserRepository;
import com.Bruno.LifeHub.services.exceptions.EmailAlreadyExistsException;
import com.Bruno.LifeHub.services.exceptions.ResourceNotFoundException;

@Service
public class UserService {

	private final UserRepository repository;
	private final PasswordEncoder passwordEnconder;

	public UserService(UserRepository repository, PasswordEncoder passwordEnconder) {
		this.repository = repository;
		this.passwordEnconder = passwordEnconder;
	}

	@Transactional(readOnly = true)
	public Page<UserDTO> findAllPaged(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public UserDTO findByEmail(String email) {
		User result = repository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Entidade não encontrada"));
		return new UserDTO(result);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO dto) {

		if (repository.existsByEmail(dto.getEmail())) {
			throw new EmailAlreadyExistsException("Email já existe");
		}

		User user = new User();
		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(passwordEnconder.encode(dto.getPassword()));

		user = repository.save(user);

		return new UserDTO(user);
	}
}