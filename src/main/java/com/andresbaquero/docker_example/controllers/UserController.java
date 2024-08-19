package com.andresbaquero.docker_example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andresbaquero.docker_example.dto.CreateUserDTO;
import com.andresbaquero.docker_example.dto.UpdateUserDTO;
import com.andresbaquero.docker_example.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping(path = "")
    public ResponseEntity<?> findAllUsers() {
        return service.findAllUsers();
    }

    @PostMapping(path = "", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO request) {
        return service.createUser(request);
    }

    @PatchMapping(path = "/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UpdateUserDTO request) {
        return service.updateUser(id, request.getUser());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        return service.deleteUser(id);
    }

}
