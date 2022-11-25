package tech.antoniosgarbi.desafioapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tech.antoniosgarbi.desafioapi.model.Tag;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    private Long id;
    private  String value;
    private Long accessCount;

    public TagDTO(Tag entity) {
        this.id = entity.getId();
        this.value = entity.getTag();
        this.accessCount = entity.getAccessCount();
    }

}
