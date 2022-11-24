package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tech.antoniosgarbi.desafioapi.dto.IntegrationDTO;
import tech.antoniosgarbi.desafioapi.exception.DateException;
import tech.antoniosgarbi.desafioapi.exception.ExternalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class IntegrationServiceImplTest {

    @Mock
    private RestTemplate template;
    @InjectMocks
    private IntegrationServiceImpl underTest;


    @Test
    @DisplayName("Should throws DateException when receives invalid date")
    void query1() {
        var e =
                assertThrows(DateException.class, () -> this.underTest.query("anything", "aa/11/2022"));

        String message = "A data passada deve estar no formato dd/mm/aaaa ou a busca deve ser feita sem o parametro de data";
        assertEquals(message, e.getMessage());
    }

    @Test
    @DisplayName("Should throws DateException when receives invalid date")
    void query2() {
        var e =
                assertThrows(DateException.class, () -> this.underTest.query("anything", "3412/13/2022"));

        String message = "A data passada deve estar no formato dd/mm/aaaa ou a busca deve ser feita sem o parametro de data";
        assertEquals(message, e.getMessage());
    }

    @Test
    @DisplayName("Should throws when external api receives invalid response")
    void query3() {
        when(this.template.exchange(anyString(),any(HttpMethod.class),any(HttpEntity.class),any(Class.class)))
                .thenThrow(new RestClientException("error"));

        var e = assertThrows(ExternalException.class, () ->
                this.underTest.query("anything", "03/12/2022"));

        assertEquals("Não foi possível obter os resultados, tente novamente", e.getMessage());
    }

    @Test
    @DisplayName("Should return IntegrationDTO when external api response is valid")
    void query4() {
        when(this.template.exchange(anyString(),any(HttpMethod.class),any(HttpEntity.class),any(Class.class)))
                .thenReturn(ResponseEntity.ok(new IntegrationDTO()));

        IntegrationDTO response = this.underTest.query("anything", "03/12/2022");

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should return IntegrationDTO when receives date null and external api response is valid")
    void query5() {
        when(this.template.exchange(anyString(),any(HttpMethod.class),any(HttpEntity.class),any(Class.class)))
                .thenReturn(ResponseEntity.ok(new IntegrationDTO()));

        IntegrationDTO response = this.underTest.query("anything", null);

        assertNotNull(response);
    }
}