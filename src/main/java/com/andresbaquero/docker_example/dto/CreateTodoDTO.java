package com.andresbaquero.docker_example.dto;

import java.util.Date;

import com.andresbaquero.docker_example.models.TodoModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTodoDTO {

    @NotBlank(message = "La descripción del TODO es obligatoria.")
    private String description;

    public TodoModel getTodo(String user) {
        TodoModel todo = new TodoModel();
        todo.setDescription(description);
        todo.setCompleted(false);
        todo.setUser(user);
        todo.setTimestamp(new Date());

        return todo;
    }

}
