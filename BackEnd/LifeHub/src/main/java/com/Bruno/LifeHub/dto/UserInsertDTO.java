package com.Bruno.LifeHub.dto;

import java.io.Serializable;

import com.Bruno.LifeHub.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInsertDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres")
    private String password;

    public UserInsertDTO() {
    }

    public UserInsertDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
