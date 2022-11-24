package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.dto.LoginDTO;
import tech.antoniosgarbi.desafioapi.dto.RefreshDTO;
import tech.antoniosgarbi.desafioapi.dto.TokenDTO;
import tech.antoniosgarbi.desafioapi.service.LoginService;
import tech.antoniosgarbi.desafioapi.service.TokenService;


@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getSenha())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String accessToken = tokenService.generateAccessToken(userDetails);
        String refreshToken = tokenService.generateRefreshToken(userDetails);

        return new TokenDTO(accessToken, refreshToken);
    }

    @Override
    public TokenDTO refresh(RefreshDTO refreshDTO) {
        String token = refreshDTO.getRefreshToken();

        if(tokenService.validateRefreshToken(token)) {
            String username = tokenService.getUsernameFromToken(token);

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            String accessToken = tokenService.generateAccessToken(userDetails);
            String refreshToken = tokenService.generateRefreshToken(userDetails);

            return new TokenDTO(accessToken, refreshToken);
        } else {
            throw new BadCredentialsException("Refresh token expirado, faça login novamente");
        }
    }

}
