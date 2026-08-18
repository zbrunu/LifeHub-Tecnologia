package com.Bruno.LifeHub.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.Bruno.LifeHub.dto.UserDTO;
import com.Bruno.LifeHub.dto.UserInsertDTO;
import com.Bruno.LifeHub.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserResource.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserResourceTests {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService service;

    @Test
    void findAllDeveRetornarUsuarios() throws Exception {

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");

        Page<UserDTO> page = new PageImpl<>(
                List.of(user),
                PageRequest.of(0, 10),
                1
        );

        when(service.findAllPaged(any(Pageable.class)))
                .thenReturn(page);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].name").value("Bruno"))
                .andExpect(jsonPath("$.content[0].email").value("bruno@email.com"));

        verify(service).findAllPaged(any(Pageable.class));
    }

    @Test
    void findByEmailDeveRetornarUsuarioQuandoEmailExiste() throws Exception {

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");

        when(service.findByEmail("bruno@email.com"))
                .thenReturn(user);

        mockMvc.perform(
                get("/users/{email}", "bruno@email.com")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bruno"))
                .andExpect(jsonPath("$.email").value("bruno@email.com"));

        verify(service).findByEmail("bruno@email.com");
    }

    @Test
    void insertDeveRetornarCreatedQuandoDadosValidos() throws Exception {

        UserInsertDTO dto = new UserInsertDTO(
                "Bruno",
                "bruno@email.com",
                "12345678"
        );

        UserDTO user = new UserDTO();
        user.setId(1L);
        user.setName("Bruno");
        user.setEmail("bruno@email.com");

        when(service.insert(any(UserInsertDTO.class)))
                .thenReturn(user);

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/users/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Bruno"))
                .andExpect(jsonPath("$.email").value("bruno@email.com"));

        verify(service).insert(any(UserInsertDTO.class));
    }

    @Test
    void insertDeveRetornarBadRequestQuandoDadosInvalidos() throws Exception {

        UserInsertDTO dto = new UserInsertDTO(
                "",
                "email-invalido",
                ""
        );

        mockMvc.perform(
                post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isBadRequest());
    }
}