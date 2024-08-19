package com.andresbaquero.docker_example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andresbaquero.docker_example.dto.LoginAccountDTO;
import com.andresbaquero.docker_example.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping(path = "login", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> login(@Valid @RequestBody LoginAccountDTO request) {
        return service.login(request);
    }

}
