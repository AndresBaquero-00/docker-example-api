package com.andresbaquero.docker_example.dto;

import com.andresbaquero.docker_example.models.TodoModel;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateTodoDTO {

    @NotBlank(message = "La descripción del TODO es obligatoria.")
    private String description;

    public TodoModel getTodo() {
        TodoModel todo = new TodoModel();
        todo.setDescription(description);

        return todo;
    }

}
