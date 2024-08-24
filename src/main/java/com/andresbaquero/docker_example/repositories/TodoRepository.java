package com.andresbaquero.docker_example.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.andresbaquero.docker_example.models.TodoModel;

public interface TodoRepository extends ElasticsearchRepository<TodoModel, String> {

    public Iterable<TodoModel> findAllByUser(String user);

}
