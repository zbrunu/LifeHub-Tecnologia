package com.Bruno.LifeHub.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Bruno.LifeHub.dto.UserDTO;
import com.Bruno.LifeHub.dto.UserInsertDTO;
import com.Bruno.LifeHub.entities.User;
import com.Bruno.LifeHub.repositories.UserRepository;
import com.Bruno.LifeHub.services.exceptions.EmailAlreadyExistsException;
import com.Bruno.LifeHub.services.exceptions.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService service;

    @Test
    void findAllPagedDeveRetornarPageDeUserDTO() {

        User user = new User();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");
        user.setPassword("123456");

        List<User> users = List.of(user);

        Page<User> page = new PageImpl<>(users);

        Pageable pageable = PageRequest.of(0, 10);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<UserDTO> result = service.findAllPaged(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Bruno", result.getContent().get(0).getName());
        assertEquals("bruno@email.com", result.getContent().get(0).getEmail());

        verify(repository).findAll(pageable);
    }

    @Test
    void findByEmailDeveRetornarUserDTOQuandoEmailExiste() {

        User user = new User();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");
        user.setPassword("123456");

        when(repository.findByEmail("bruno@email.com"))
                .thenReturn(Optional.of(user));

        UserDTO result = service.findByEmail("bruno@email.com");

        assertEquals(1L, result.getId());
        assertEquals("Bruno", result.getName());
        assertEquals("bruno@email.com", result.getEmail());

        verify(repository).findByEmail("bruno@email.com");
    }

    @Test
    void findByEmailDeveLancarExcecaoQuandoEmailNaoExiste() {

        when(repository.findByEmail("naoexiste@email.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findByEmail("naoexiste@email.com")
        );

        verify(repository).findByEmail("naoexiste@email.com");
    }

    @Test
    void insertDeveCadastrarUsuarioQuandoEmailNaoExiste() {

        UserInsertDTO dto = new UserInsertDTO(
                "Bruno",
                "bruno@email.com",
                "123456"
        );

        User user = new User();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");
        user.setPassword("senhaCriptografada");

        when(repository.existsByEmail("bruno@email.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaCriptografada");

        when(repository.save(any(User.class)))
                .thenReturn(user);

        UserDTO result = service.insert(dto);

        assertEquals(1L, result.getId());
        assertEquals("Bruno", result.getName());
        assertEquals("bruno@email.com", result.getEmail());

        verify(repository).existsByEmail("bruno@email.com");
        verify(passwordEncoder).encode("123456");
        verify(repository).save(any(User.class));
    }

    @Test
    void insertDeveLancarExcecaoQuandoEmailJaExiste() {

        UserInsertDTO dto = new UserInsertDTO(
                "Bruno",
                "bruno@email.com",
                "123456"
        );

        when(repository.existsByEmail("bruno@email.com"))
                .thenReturn(true);

        assertThrows(
                EmailAlreadyExistsException.class,
                () -> service.insert(dto)
        );

        verify(repository).existsByEmail("bruno@email.com");

        verify(passwordEncoder, never()).encode(anyString());
        verify(repository, never()).save(any(User.class));
    }

    @Test
    void insertDeveCriptografarSenhaAntesDeSalvar() {

        UserInsertDTO dto = new UserInsertDTO(
                "Bruno",
                "bruno@email.com",
                "123456"
        );

        when(repository.existsByEmail("bruno@email.com"))
                .thenReturn(false);

        when(passwordEncoder.encode("123456"))
                .thenReturn("senhaCriptografada");

        when(repository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);

                    user.setId(1L);

                    return user;
                });

        UserDTO result = service.insert(dto);

        assertEquals("Bruno", result.getName());
        assertEquals("bruno@email.com", result.getEmail());

        verify(passwordEncoder).encode("123456");

        verify(repository).save(any(User.class));
    }
}