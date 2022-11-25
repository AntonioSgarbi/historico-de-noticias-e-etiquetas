package tech.antoniosgarbi.desafioapi.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccessTagRegisterDTO {

    private Long id;
    private String tag;
    private Date date;

}
