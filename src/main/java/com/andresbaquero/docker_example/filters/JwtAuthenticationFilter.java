package com.andresbaquero.docker_example.filters;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.andresbaquero.docker_example.models.AccountModel;
import com.andresbaquero.docker_example.models.AuthenticatedModel;
import com.andresbaquero.docker_example.models.UserModel;
import com.andresbaquero.docker_example.repositories.AccountRepository;
import com.andresbaquero.docker_example.repositories.UserRepository;
import com.andresbaquero.docker_example.services.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private JwtService jwtService;
    private AccountRepository accountRepository;
    private UserRepository userRepository;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService,
            AccountRepository accountRepository, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtService = jwtService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtService.HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(JwtService.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(JwtService.PREFIX_TOKEN, "");
        Claims claims = jwtService.verifyToken(token);
        if (claims == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        if (!request.getRemoteAddr().equals(claims.get("remote"))) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        AccountModel account = accountRepository.findById(claims.getSubject()).get();
        UserModel user = userRepository.findById((String) claims.get("user")).get();
        UsernamePasswordAuthenticationToken at = new UsernamePasswordAuthenticationToken(null, null,
                account.getAuthorities());
        at.setDetails(new AuthenticatedModel(account, user));

        SecurityContextHolder.getContext().setAuthentication(at);
        chain.doFilter(request, response);
    }

}
