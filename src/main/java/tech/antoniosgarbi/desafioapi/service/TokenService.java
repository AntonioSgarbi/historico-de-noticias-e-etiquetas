package tech.antoniosgarbi.desafioapi.service;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.security.core.userdetails.UserDetails;


public interface TokenService {

    Algorithm algorithm(String secret);

    String generateAccessToken(UserDetails userDetails);

    boolean validateAccessToken(String token);

    boolean validateRefreshToken(String token);

    String generateRefreshToken(UserDetails userDetails);

    String getUsernameFromToken(String jwt);
}
