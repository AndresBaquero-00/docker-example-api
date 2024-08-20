package com.andresbaquero.docker_example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andresbaquero.docker_example.repositories.AccountRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String message = "Usuario o contraseña son incorrectos.";
        return accountRepository
                .findOneByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(message));
    }

}
