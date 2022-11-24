package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import tech.antoniosgarbi.desafioapi.dto.LoginDTO;
import tech.antoniosgarbi.desafioapi.dto.RefreshDTO;
import tech.antoniosgarbi.desafioapi.dto.TokenDTO;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class LoginServiceImplTest {

    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsService userDetailsService;
    @InjectMocks
    private LoginServiceImpl underTest;


    @Test
    @DisplayName("Should return TokenDTO when receives valid login")
    void login1() {
        User user = new User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        UsernamePasswordAuthenticationToken expected = new UsernamePasswordAuthenticationToken(user, null);

        when(this.authenticationManager.authenticate(any(Authentication.class))).thenReturn(expected);

        String accessTokenExpected = "accessToken";
        when(this.tokenService.generateAccessToken(any(UserDetails.class))).thenReturn(accessTokenExpected);

        String refreshTokenExpected = "refreshToken";
        when(this.tokenService.generateRefreshToken(any(UserDetails.class))).thenReturn(refreshTokenExpected);

        TokenDTO response = this.underTest.login(new LoginDTO());

        assertEquals(response.getAccessToken(), accessTokenExpected);
        assertEquals(response.getRefreshToken(), refreshTokenExpected);
    }

    @Test
    @DisplayName("Should throws BadCredentialsException when receives wrong username or password")
    void login2() {
        when(this.authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("error"));

        assertThrows(BadCredentialsException.class, () ->
                this.underTest.login(new LoginDTO()));
    }

    @Test
    @DisplayName("Should throws BadCredentialsException when refresh token is invalid")
    void refresh1() {
        when(this.tokenService.validateRefreshToken(anyString())).thenReturn(false);

        var e = assertThrows(BadCredentialsException.class, () -> this.underTest.refresh(new RefreshDTO()));

        assertEquals("Refresh token expirado, faça login novamente", e.getMessage());
    }

    @Test
    @DisplayName("Should return TokenDTO when reveices valid refresh token")
    void refresh3() {
        when(this.tokenService.validateRefreshToken(anyString())).thenReturn(true);

        String accessTokenExpected = "accessToken";
        when(this.tokenService.generateAccessToken(any())).thenReturn(accessTokenExpected);

        String refreshTokenExpected = "refreshToken";
        when(this.tokenService.generateRefreshToken(any())).thenReturn(refreshTokenExpected);

        TokenDTO response = this.underTest.refresh(new RefreshDTO("a"));

        assertEquals(accessTokenExpected, response.getAccessToken());
        assertEquals(refreshTokenExpected, response.getRefreshToken());
    }
}