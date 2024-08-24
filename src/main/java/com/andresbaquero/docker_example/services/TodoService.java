package com.andresbaquero.docker_example.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.andresbaquero.docker_example.dto.CreateTodoDTO;
import com.andresbaquero.docker_example.models.TodoModel;
import com.andresbaquero.docker_example.repositories.TodoRepository;

@Service
public class TodoService {

    Logger logger = LoggerFactory.getLogger(TodoService.class);

    @Autowired
    private TodoRepository todoRepository;

    public ResponseEntity<?> findAllTodosByUser(String user) {
        logger.info("Listando TODOs del usuario " + user);
        List<TodoModel> todos = (List<TodoModel>) todoRepository.findAllByUser(user);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    public ResponseEntity<?> createTodo(CreateTodoDTO request, String user) {
        logger.info("Creando TODO para el usuario " + user);
        TodoModel todo = request.getTodo(user);
        TodoModel saved = todoRepository.save(todo);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateTodo(String id, TodoModel request) {
        logger.info("Actualizando el TODO " + id);
        Optional<TodoModel> saved = todoRepository.findById(id);
        if (saved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TodoModel todo = saved.get();
        todo.setDescription(request.getDescription());
        todoRepository.save(todo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> markTodoAsCompletedUncompleted(String id) {
        logger.info("Realizando marcado de completo-incompleto del TODO " + id);
        Optional<TodoModel> saved = todoRepository.findById(id);
        if (saved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        TodoModel todo = saved.get();
        todo.setCompleted(!todo.getCompleted());
        todoRepository.save(todo);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
