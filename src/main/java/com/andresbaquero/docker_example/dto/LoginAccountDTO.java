package com.andresbaquero.docker_example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginAccountDTO {

    @Email(message = "El email del usuario no es válido.")
    @NotBlank(message = "El email del usuario es obligatorio.")
    private String email;

    @NotBlank(message = "La contraseña del usuario es obligatoria.")
    private String password;

}
