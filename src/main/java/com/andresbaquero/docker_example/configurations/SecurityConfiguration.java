package com.andresbaquero.docker_example.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.andresbaquero.docker_example.filters.AuthenticationFilter;
import com.andresbaquero.docker_example.filters.JwtAuthenticationFilter;
import com.andresbaquero.docker_example.repositories.AccountRepository;
import com.andresbaquero.docker_example.services.JwtService;

import jakarta.servlet.Filter;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AccountRepository accountRepository;

    private Filter getFilter() throws Exception {
        return new AuthenticationFilter(getAuthenticationManager(), jwtService, accountRepository);
    }

    private Filter getJwtFilter() throws Exception {
        return new JwtAuthenticationFilter(getAuthenticationManager(), jwtService, accountRepository);
    }

    @Bean
    AuthenticationManager getAuthenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/users")
                .permitAll()
                .requestMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated())
                .addFilter(getFilter())
                .addFilter(getJwtFilter())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

}
