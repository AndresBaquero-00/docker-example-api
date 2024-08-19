package com.andresbaquero.docker_example.services;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andresbaquero.docker_example.dto.LoginAccountDTO;
import com.andresbaquero.docker_example.models.AccountModel;
import com.andresbaquero.docker_example.repositories.AccountRepository;

@Service
public class AuthService {

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AccountRepository accountRepository;

    public ResponseEntity<?> login(LoginAccountDTO request) {
        Optional<AccountModel> exists = accountRepository.findOneByEmail(request.getEmail());
        if (!exists.isPresent()) {
            String message = "Usuario o contraseña son incorrectos.";
            return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.FORBIDDEN);
        }

        AccountModel account = exists.get();
        boolean isValid = encoder.matches(request.getPassword(), account.getPassword());
        if (!isValid) {
            String message = "Usuario o contraseña son incorrectos.*";
            return new ResponseEntity<>(Collections.singletonMap("message", message), HttpStatus.FORBIDDEN);
        }

        account.setLogins(account.getLogins() + 1);
        account.setLastLogin(new Date());
        accountRepository.save(account, RefreshPolicy.IMMEDIATE);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
