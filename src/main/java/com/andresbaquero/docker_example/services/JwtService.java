package com.andresbaquero.docker_example.services;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class JwtService {

    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public static final Long DURATION = 3600000L;
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    public Claims verifyToken(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .verifyWith(JwtService.SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims;
        } catch (JwtException e) {
            return null;
        }

    }

    public String generateToken(String id, String userId, String remote, List<String> roles) {
        Claims claims;
        try {
            claims = Jwts.claims()
                    .add("user", userId)
                    .add("remote", remote)
                    .add("authorities", new ObjectMapper().writeValueAsString(roles))
                    .build();

            String token = Jwts.builder()
                    .subject(id)
                    .claims(claims)
                    .expiration(new Date(System.currentTimeMillis() + JwtService.DURATION))
                    .issuedAt(new Date())
                    .signWith(JwtService.SECRET_KEY)
                    .compact();

            return token;
        } catch (JsonProcessingException e) {
            return null;
        }
    }

}
