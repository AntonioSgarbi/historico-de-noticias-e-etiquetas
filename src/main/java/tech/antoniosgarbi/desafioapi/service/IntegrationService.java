package tech.antoniosgarbi.desafioapi.service;

import tech.antoniosgarbi.desafioapi.dto.IntegrationDTO;


public interface IntegrationService {

    IntegrationDTO query(String query, String date);

}
