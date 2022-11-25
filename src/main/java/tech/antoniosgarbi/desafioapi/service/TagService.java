package tech.antoniosgarbi.desafioapi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.model.Tag;


public interface TagService {

    Tag createTagIfNotExists(String value);

    Page<TagDTO> findAllOrderByAccessCount(Pageable pageable);

    Tag save(Tag tag);

}
