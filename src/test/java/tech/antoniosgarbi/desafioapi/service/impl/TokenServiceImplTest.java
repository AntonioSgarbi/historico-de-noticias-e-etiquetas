package tech.antoniosgarbi.desafioapi.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
class TokenServiceImplTest {

    private static MockedStatic<JWT> jwt;
    private static MockedStatic<Algorithm> staticAlgorithm;
    private static MockedStatic<JWTCreator.Builder> staticJWTBuilder;
    @Mock
    private JWTCreator.Builder builder;
    @Mock
    private Algorithm algorithm;
    @Mock
    private DecodedJWT decode;
    @Mock
    private Verification verification;
    @Mock
    private JWTVerifier jwtVerifier;
    @InjectMocks
    private TokenServiceImpl underTest;

    @BeforeEach
    void setUp() {
        this.openStatic();

    }

    @AfterEach
    void tearDown() {
        this.closeStatic();
    }


    @Test
    @DisplayName("Should return Algorithm when receives secret")
    void algorithm1() {
        Algorithm expected = Algorithm.HMAC256("secret");
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(expected);

        Algorithm response = this.underTest.algorithm("secret");

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws IllegalArgumentException when receives null secret")
    void algorithm2() {
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenThrow(new IllegalArgumentException("error"));

        assertThrows(IllegalArgumentException.class, () -> this.underTest.algorithm("secret"));
    }

    @Test
    @DisplayName("Should return accessToken when successfull")
    void generateAccessToken1() {
        ReflectionTestUtils.setField(underTest, "accessExpiration", 10L);

        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.create()).thenReturn(builder);

        when(builder.withSubject(any())).thenReturn(builder);
        when(builder.withExpiresAt(any())).thenReturn(builder);
        when(builder.withIssuer(any())).thenReturn(builder);
        String expected = "token";
        when(builder.sign(any())).thenReturn(expected);

        User user = new User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String response = this.underTest.generateAccessToken(user);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws IllegalArgumentException when receives null algorithm")
    void generateAccessToken2() {
        ReflectionTestUtils.setField(underTest, "accessExpiration", 10L);

        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.create()).thenReturn(builder);

        when(builder.withSubject(any())).thenReturn(builder);
        when(builder.withExpiresAt(any())).thenReturn(builder);
        when(builder.withIssuer(any())).thenReturn(builder);
        String expected = "token";
        when(builder.sign(any())).thenThrow(new JWTCreationException("error", null));

        User user = new User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        assertThrows(JWTCreationException.class, () -> this.underTest.generateAccessToken(user));
    }

    @Test
    @DisplayName("Should return false if token is null")
    void validateAccessToken1() {
        assertFalse(this.underTest.validateAccessToken(null));
    }

    @Test
    @DisplayName("Should return false if jwt throws JWTVerificationException")
    void validateAccessToken2() {
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.require(any()).build()).thenReturn(verification);

        when(verification.withIssuer(any())).thenReturn(verification);

        when(verification.build()).thenReturn(jwtVerifier);

        when(jwtVerifier.verify(anyString())).thenThrow(new JWTVerificationException("error"));

        assertFalse(this.underTest.validateAccessToken("token"));
    }

    @Test
    @DisplayName("Should return true when receives valid token")
    void validateAccessToken3() {
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.require(any()).build()).thenReturn(verification);

        when(verification.withIssuer(any())).thenReturn(verification);

        when(verification.build()).thenReturn(jwtVerifier);

        when(jwtVerifier.verify(anyString())).thenReturn(decode);

        assertTrue(this.underTest.validateAccessToken("token"));
    }

    @Test
    @DisplayName("Should return refreshToken when successfull")
    void generateRefreshToken1() {
        ReflectionTestUtils.setField(underTest, "refreshExpiration", 10L);

        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.create()).thenReturn(builder);

        when(builder.withSubject(any())).thenReturn(builder);
        when(builder.withExpiresAt(any())).thenReturn(builder);
        when(builder.withIssuer(any())).thenReturn(builder);
        String expected = "token";
        when(builder.sign(any())).thenReturn(expected);

        User user = new User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        String response = this.underTest.generateRefreshToken(user);
        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws IllegalArgumentException when receives null algorithm")
    void generateRefreshToken2() {
        ReflectionTestUtils.setField(underTest, "refreshExpiration", 10L);

        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.create()).thenReturn(builder);

        when(builder.withSubject(any())).thenReturn(builder);
        when(builder.withExpiresAt(any())).thenReturn(builder);
        when(builder.withIssuer(any())).thenReturn(builder);
        String expected = "token";
        when(builder.sign(any())).thenThrow(new JWTCreationException("error", null));

        User user = new User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));

        assertThrows(JWTCreationException.class, () -> this.underTest.generateRefreshToken(user));
    }

    @Test
    @DisplayName("Should return false if token is null")
    void validateRefreshToken1() {
        assertFalse(this.underTest.validateRefreshToken(null));
    }

    @Test
    @DisplayName("Should return false if jwt throws JWTVerificationException")
    void validateRefreshToken2() {
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.require(any()).build()).thenReturn(verification);

        when(verification.withIssuer(any())).thenReturn(verification);

        when(verification.build()).thenReturn(jwtVerifier);

        when(jwtVerifier.verify(anyString())).thenThrow(new JWTVerificationException("error"));

        assertFalse(this.underTest.validateRefreshToken("token"));
    }

    @Test
    @DisplayName("Should return true when receives valid token")
    void validateRefreshToken3() {
        staticAlgorithm.when(() -> Algorithm.HMAC256(anyString())).thenReturn(algorithm);

        jwt.when(() -> JWT.require(any()).build()).thenReturn(verification);

        when(verification.withIssuer(any())).thenReturn(verification);

        when(verification.build()).thenReturn(jwtVerifier);

        when(jwtVerifier.verify(anyString())).thenReturn(decode);

        assertTrue(this.underTest.validateRefreshToken("token"));
    }

    @Test
    @DisplayName("Should return username when receives valid token")
    void getUsernameFromToken1() {
        String expected = "subject";

        jwt.when(() -> JWT.decode(anyString()).getSubject()).thenReturn(decode);

        when(this.decode.getSubject()).thenReturn(expected);

        String response = this.underTest.getUsernameFromToken("token");

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws JWTDecodeException when receives invalid token")
    void getUsernameFromToken2() {
        jwt.when(() -> JWT.decode(anyString()).getSubject()).thenThrow(new JWTDecodeException("error"));

        assertThrows(JWTDecodeException.class, () -> this.underTest.getUsernameFromToken("token"));
    }

    private void openStatic() {
        jwt = mockStatic(JWT.class);
        staticAlgorithm = mockStatic(Algorithm.class);
        staticJWTBuilder = mockStatic(JWTCreator.Builder.class);
    }

    private void closeStatic() {
        jwt.close();
        staticAlgorithm.close();
        staticJWTBuilder.close();
    }

}