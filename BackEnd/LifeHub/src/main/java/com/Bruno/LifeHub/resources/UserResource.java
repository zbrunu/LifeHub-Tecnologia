package com.Bruno.LifeHub.resources;


import java.net.URI;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.Bruno.LifeHub.dto.UserDTO;
import com.Bruno.LifeHub.dto.UserInsertDTO;
import com.Bruno.LifeHub.services.UserService;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

	private UserService services;
	
	public UserResource(UserService services) {
		this.services = services;
	}
	
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAllPage(Pageable pageable){
		Page<UserDTO> dto = services.findAllPaged(pageable);
		return ResponseEntity.ok().body(dto);
	}
	
	@GetMapping(value = "/{email}")
	public ResponseEntity<UserDTO> findByEmail(@PathVariable String email){
		UserDTO dto = services.findByEmail(email);
		return ResponseEntity.ok().body(dto);
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserInsertDTO dto){
		
		UserDTO userDTO = services.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
}
