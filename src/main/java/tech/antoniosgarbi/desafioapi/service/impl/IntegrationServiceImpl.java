package tech.antoniosgarbi.desafioapi.service.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import tech.antoniosgarbi.desafioapi.dto.IntegrationDTO;
import tech.antoniosgarbi.desafioapi.exception.DateException;
import tech.antoniosgarbi.desafioapi.exception.ExternalException;
import tech.antoniosgarbi.desafioapi.service.IntegrationService;

import java.time.LocalDate;


@Service
public class IntegrationServiceImpl implements IntegrationService {

    private RestTemplate template;


    public IntegrationServiceImpl() {
        this.template = new RestTemplate();
    }

    @Override
    public IntegrationDTO query(String query, String date) {
        if (date == null) {
            date = this.getDateToday();
        } else {
            try {
                String[] dates = date.split("/");
                LocalDate.of(Integer.parseInt(dates[2]), Integer.parseInt(dates[1]), Integer.parseInt(dates[0]));
            } catch (Exception e) {
                throw new DateException("A data passada deve estar no formato dd/mm/aaaa ou a busca deve ser feita sem o parametro de data");
            }
        }
        return this.makeRequest(query.replace(" ", ""), date);
    }

    @Override
    public String getDateToday() {
        LocalDate localDate = LocalDate.now();

        int day = localDate.getDayOfMonth();
        int month = localDate.getMonthValue();
        int year = localDate.getYear();

        return String.format("%d/%d/%d", day, month, year);
    }

    private IntegrationDTO makeRequest(String query, String date) {
        final String url = String.format("https://apinoticias.tedk.com.br/api/?q=%s&date=%s", query, date);
        try{
            final ResponseEntity<IntegrationDTO> response = template.exchange(url, HttpMethod.GET, this.httpEntity(), IntegrationDTO.class);
            return response.getBody();

        } catch (RestClientException e) {
            throw new ExternalException("Não foi possível obter os resultados, tente novamente");
        }
    }

    private HttpEntity<?> httpEntity() {
        final HttpHeaders headers = new HttpHeaders();
        final String USER_AGENT_HEADER = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36";
        headers.add("user-agent", USER_AGENT_HEADER);
        return new HttpEntity<>(headers);
    }

}
