package com.example.hometask.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;

import javax.crypto.SecretKey;

public class JwtKeyProvider {
    @Getter
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

}