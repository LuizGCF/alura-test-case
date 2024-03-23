package com.alura.coursecase.services;

import com.alura.coursecase.enums.ErrorsEnum;
import com.alura.coursecase.exception.InvalidTokenException;
import com.alura.coursecase.exception.TokenGenerationFailedException;
import com.alura.coursecase.models.UserModel;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    private static final String ISSUER = "alura-course-case";

    public String generateToken(UserModel user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create().withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDate())
                    .sign(algorithm);

            return token;
        } catch (RuntimeException exception) {
            throw new TokenGenerationFailedException(ErrorsEnum.A001.code, ErrorsEnum.A001.message);

        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch(JWTVerificationException exception){
            throw new InvalidTokenException(ErrorsEnum.A000.code, ErrorsEnum.A000.message);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
