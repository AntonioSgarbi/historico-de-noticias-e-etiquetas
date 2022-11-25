package tech.antoniosgarbi.desafioapi.dto;

import lombok.Data;

@Data
public class NewsDTO {

    private String title;
    private String link;
    private String date;

    public NewsDTO(NewsIntegrationDTO integrationDTO) {
        this.title = integrationDTO.getTitle();
        this.link = integrationDTO.getLink();
        this.date = integrationDTO.getDatetime();
    }

}
