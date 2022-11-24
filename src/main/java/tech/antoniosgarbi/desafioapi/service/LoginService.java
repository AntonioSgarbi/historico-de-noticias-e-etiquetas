package tech.antoniosgarbi.desafioapi.service;

import tech.antoniosgarbi.desafioapi.dto.LoginDTO;
import tech.antoniosgarbi.desafioapi.dto.RefreshDTO;
import tech.antoniosgarbi.desafioapi.dto.TokenDTO;


public interface LoginService {

    TokenDTO login(LoginDTO loginDTO);

    TokenDTO refresh(RefreshDTO refreshDTO);

}
