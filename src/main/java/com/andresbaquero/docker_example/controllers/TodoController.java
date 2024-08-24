package com.andresbaquero.docker_example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andresbaquero.docker_example.dto.CreateTodoDTO;
import com.andresbaquero.docker_example.dto.UpdateTodoDTO;
import com.andresbaquero.docker_example.models.AuthenticatedModel;
import com.andresbaquero.docker_example.services.TodoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/todos")
public class TodoController {

    @Autowired
    private TodoService service;

    private String getUserId() {
        AuthenticatedModel auth = (AuthenticatedModel) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getDetails();

        return auth.getUserId();
    }

    @GetMapping(path = "")
    public ResponseEntity<?> findAllTodos() {
        String user = getUserId();
        return service.findAllTodosByUser(user);
    }

    @GetMapping(path = "/mark/{id}")
    public ResponseEntity<?> markTodo(@PathVariable String id) {
        return service.markTodoAsCompletedUncompleted(id);
    }

    @PostMapping(path = "", consumes = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<?> createTodo(@Valid @RequestBody CreateTodoDTO request) {
        String user = getUserId();
        return service.createTodo(request, user);
    }

    @PatchMapping(path = "/{id}" )
    public ResponseEntity<?> updateTodo(@PathVariable String id, @RequestBody UpdateTodoDTO request) {
        return service.updateTodo(id, request.getTodo());
    }

}
