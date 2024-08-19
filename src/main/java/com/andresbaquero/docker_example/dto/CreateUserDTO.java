package com.andresbaquero.docker_example.dto;

import java.util.Date;

import com.andresbaquero.docker_example.models.AccountModel;
import com.andresbaquero.docker_example.models.UserModel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotBlank(message = "El nombre del usuario es obligatorio.")
    private String name;

    @NotBlank(message = "El apellido del usuario es obligatorio.")
    private String lastName;

    @Email(message = "El email del usuario no es válido.")
    @NotBlank(message = "El email del usuario es obligatorio.")
    private String email;

    @Size(min = 8, message = "La contraseña debe tener mínimo 8 caracteres.")
    @NotBlank(message = "La contraseña del usuario es obligatoria.")
    private String password;

    @Past
    @NotNull(message = "La fecha de nacimiento del usuario es obligatoria.")
    private Date birthDate;

    public UserModel getUser() {
        UserModel user = new UserModel();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        user.setTimestamp(new Date());

        return user;
    }

    public AccountModel getAccount() {
        AccountModel account = new AccountModel();
        account.setEmail(email);
        account.setPassword(password);
        account.setLogins(0L);
        account.setState(AccountModel.ACTIVE);
        account.setLastLogin(null);
        account.setTimestamp(new Date());

        return account;
    }

}
