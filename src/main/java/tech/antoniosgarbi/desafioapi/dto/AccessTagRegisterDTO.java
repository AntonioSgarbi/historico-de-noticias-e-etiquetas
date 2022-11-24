package tech.antoniosgarbi.desafioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessTagRegisterDTO {

    private Long id;
    private String tag;
    private Date date;

}
