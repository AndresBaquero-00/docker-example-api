package com.andresbaquero.docker_example.models;

import java.util.List;

import lombok.Getter;

@Getter
public class AuthenticatedModel {

    private String accountId;
    private String userId;
    private String fullName;
    private String email;
    private List<String> roles;


    public AuthenticatedModel(AccountModel account, UserModel user) {
        this.accountId = account.getId();
        this.userId = user.getId();
        this.fullName = user.getName() + " " + user.getLastName();
        this.email = account.getEmail();
        this.roles = account.getRoles();
    }

}
