package com.andresbaquero.docker_example.filters;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.andresbaquero.docker_example.dto.LoginAccountDTO;
import com.andresbaquero.docker_example.models.AccountModel;
import com.andresbaquero.docker_example.models.UserModel;
import com.andresbaquero.docker_example.repositories.AccountRepository;
import com.andresbaquero.docker_example.repositories.UserRepository;
import com.andresbaquero.docker_example.services.JwtService;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public AuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
            AccountRepository accountRepository, UserRepository userRepository) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        LoginAccountDTO user = null;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), LoginAccountDTO.class);
            username = user.getEmail();
            password = user.getPassword();
        } catch (StreamReadException e) {
            logger.error(e.getMessage());
        } catch (DatabindException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        UsernamePasswordAuthenticationToken at = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(at);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        AccountModel account = (AccountModel) authResult.getPrincipal();
        account.setLogins(account.getLogins() + 1);
        account.setLastLogin(new Date());
        accountRepository.save(account, RefreshPolicy.IMMEDIATE);
        
        UserModel user = userRepository.findOneByEmail(account.getEmail()).get();

        String token = jwtService.generateToken(account.getId(), user.getId(), request.getRemoteAddr(), account.getRoles());
        Map<String, String> res = Collections.singletonMap("token", token);

        response.addHeader(JwtService.HEADER_AUTHORIZATION, JwtService.PREFIX_TOKEN + token);
        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(res));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> res = Collections.singletonMap("message", "Usuario o contraseña son incorrectos.");

        response.getWriter()
                .write(new ObjectMapper().writeValueAsString(res));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }

}
