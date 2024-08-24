package com.andresbaquero.docker_example.services;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andresbaquero.docker_example.dto.CreateUserDTO;
import com.andresbaquero.docker_example.models.AccountModel;
import com.andresbaquero.docker_example.models.UserModel;
import com.andresbaquero.docker_example.repositories.AccountRepository;
import com.andresbaquero.docker_example.repositories.UserRepository;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;

@Service
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private ElasticsearchClient es;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<?> findAllUsers() {
        logger.info("Listando usuarios");
        PageImpl<UserModel> users = (PageImpl<UserModel>) userRepository.findAll();
        return new ResponseEntity<>(users.getContent(), HttpStatus.OK);
    }

    public ResponseEntity<?> createUser(CreateUserDTO request) {
        logger.info("Creando el usuario " + request.getEmail());
        Optional<AccountModel> exists = accountRepository.findOneByEmail(request.getEmail());
        if (exists.isPresent()) {
            String message = "Ya existe un usuario con el correo electrónico diligenciado.";
            return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.CONFLICT);
        }

        AccountModel account = request.getAccount();
        String encoded = encoder.encode(account.getPassword());
        account.setPassword(encoded);
        accountRepository.save(account);

        UserModel user = request.getUser();
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateUser(String id, UserModel user) {
        logger.info("Actualizando el usuario " + id);
        Optional<UserModel> userSaved = userRepository.findById(id);
        if (userSaved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Map<String, String> doc = new HashMap<>();
            doc.put("name", user.getName() != null ? user.getName() : userSaved.get().getName());
            doc.put("lastName", user.getLastName() != null ? user.getLastName() : userSaved.get().getLastName());

            es.update(b -> b
                    .index(UserModel.INDEX)
                    .id(id)
                    .doc(doc), UserModel.class);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ElasticsearchException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("message", e.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("message", e.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteUser(String id) {
        logger.info("Eliminando el usuario " + id);
        Optional<UserModel> userSaved = userRepository.findById(id);
        if (userSaved.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            Optional<AccountModel> accountSaved = accountRepository.findOneByEmail(userSaved.get().getEmail());
            String script = "ctx._source.state = '%s'".formatted(AccountModel.STATE_INACTIVE);

            es.update(b -> b
                    .index(AccountModel.INDEX)
                    .id(accountSaved.get().getId())
                    .script(s -> s
                            .inline(si -> si
                                    .source(script))),
                    AccountModel.class);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ElasticsearchException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("message", e.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(Collections.singletonMap("message", e.toString()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
