package tech.antoniosgarbi.desafioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewsIntegrationDTO {

    private String title;
    private String description;
    private String href;
    private String link;
    private String datetime;
    private String date;
    private String time;

}
