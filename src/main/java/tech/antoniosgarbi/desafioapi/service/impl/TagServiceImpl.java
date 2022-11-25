package tech.antoniosgarbi.desafioapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.repository.TagRepository;
import tech.antoniosgarbi.desafioapi.service.TagService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;


    @Override
    public Tag createTagIfNotExists(String value) {
        Optional<Tag> optional = this.tagRepository.findByValue(value);

        Tag model;

        if(optional.isPresent()) {
            model = optional.get();
            model.setAccessCount(model.getAccessCount() + 1);
        } else {
            model = new Tag();
            model.setValue(value);
            model.setAccessCount(1L);
        }

        return this.tagRepository.save(model);
    }

    @Override
    public Page<TagDTO> findAllOrderByAccessCount(Pageable pageable) {
        return this.tagRepository.findAllByOrderByAccessCountDesc(pageable).map(TagDTO::new);
    }

    @Override
    public Tag save(Tag tag) {
        return this.tagRepository.save(tag);
    }

}
