package tech.antoniosgarbi.desafioapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.service.TokenService;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${security.token.access_expiration}")
    private Long accessExpiration;

    @Value("${security.token.refresh_expiration}")
    private Long refreshExpiration;

    @Value("${security.token.issuer}")
    private String issuer;

    @Value("${security.token.access_secret}")
    private String accessSecret;

    @Value("${security.token.refresh_secret}")
    private String refreshSecret;


    @Override
    public Algorithm algorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Date agora = new Date();
        Date expirar = new Date(agora.getTime() + accessExpiration);

        return JWT
                .create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(expirar)
                .withIssuer(this.issuer)
                .sign(this.algorithm(this.accessSecret));
    }

    @Override
    public boolean validateAccessToken(String token) {
        return this.validateToken(token, this.accessSecret);
    }

    @Override
    public boolean validateRefreshToken(String token) {
        return this.validateToken(token, this.refreshSecret);
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        Date agora = new Date();

        Date expirar = new Date(agora.getTime() + this.refreshExpiration);

        return JWT
                .create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(expirar)
                .withIssuer(this.issuer)
                .sign(this.algorithm(this.refreshSecret));
    }

    private boolean validateToken(String token, String secret) {
        if(token == null) return false;

        try {
            JWT.require(this.algorithm(secret))
                    .withIssuer(this.issuer)
                    .build()
                    .verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String jwt) {
        return JWT.decode(jwt).getSubject();
    }

}
