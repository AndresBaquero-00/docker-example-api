package com.andresbaquero.docker_example.filters;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.andresbaquero.docker_example.repositories.AccountRepository;
import com.andresbaquero.docker_example.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    // private AuthenticationManager authenticationManager;

    // private JwtService jwtService;

    // private AccountRepository accountRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
            AccountRepository accountRepository) {
        super(authenticationManager);
        // this.authenticationManager = authenticationManager;
        // this.jwtService = jwtService;
        // this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        UsernamePasswordAuthenticationToken at = new UsernamePasswordAuthenticationToken(null, null, null);
        SecurityContextHolder.getContext().setAuthentication(at);
        chain.doFilter(request, response);
    }

}
