package com.example.tutorial.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tutorial.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt_secret}")
    private String secret;

    private int tokenTime = 60; // thời gian hết hạn token 60 giây

    public String generateToken(User user) throws IllegalArgumentException, JWTCreationException {
        System.out.println(secret);
        Date expiration = new Date(System.currentTimeMillis() + tokenTime * 1000);
        return JWT.create()
                .withClaim("name", user.getName())
                .withIssuedAt(new Date())
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean validateToken(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT jwt = verifier.verify(token);
        jwt.getClaim("name").asString();
        return true;
    }

}