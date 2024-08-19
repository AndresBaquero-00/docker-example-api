package com.andresbaquero.docker_example.repositories;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.andresbaquero.docker_example.models.AccountModel;

public interface AccountRepository extends ElasticsearchRepository<AccountModel, String> {

    public Optional<AccountModel> findOneByEmail(String email);

}
