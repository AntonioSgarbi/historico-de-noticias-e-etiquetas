package tech.antoniosgarbi.desafioapi.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class IntegrationDTO {

    private Integer count;
    private List<NewsIntegrationDTO> list;

}
