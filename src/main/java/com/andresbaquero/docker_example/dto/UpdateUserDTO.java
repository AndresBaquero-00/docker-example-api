package com.andresbaquero.docker_example.dto;

import com.andresbaquero.docker_example.models.UserModel;

import lombok.Data;

@Data
public class UpdateUserDTO {

    private String name;
    private String lastName;

    public UserModel getUser() {
        UserModel user = new UserModel();
        user.setName(name);
        user.setLastName(lastName);

        return user;
    }

}
