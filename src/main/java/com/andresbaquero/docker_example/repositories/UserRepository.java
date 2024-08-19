package com.andresbaquero.docker_example.repositories;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.andresbaquero.docker_example.models.UserModel;

public interface UserRepository extends ElasticsearchRepository<UserModel, String> {

    public Optional<UserModel> findOneByEmail(String email);

}
