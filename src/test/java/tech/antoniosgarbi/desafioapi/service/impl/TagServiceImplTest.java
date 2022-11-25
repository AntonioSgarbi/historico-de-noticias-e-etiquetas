package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.repository.TagRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class TagServiceImplTest {

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private TagServiceImpl underTest;


    @Test
    @DisplayName("Should increment count when search tag already registered")
    void createTagIfNotExists1() {
        Tag tag = Tag.builder().id(1L).accessCount(10L).build();

        when(this.tagRepository.findByTag(anyString()))
                .thenReturn(Optional.of(tag));

        when(this.tagRepository.save(any(Tag.class))).then(iv -> {
            return iv.getArgument(0);
        });
        Tag response = this.underTest.createTagIfNotExists("anyString");

        assertEquals(11L, response.getAccessCount());
    }

    @Test
    @DisplayName("Should create tag when search tag not registered")
    void createTagIfNotExists2() {
        when(this.tagRepository.findByTag(anyString())).thenReturn(Optional.empty());
        when(this.tagRepository.save(any(Tag.class))).then(iv -> {
            return iv.getArgument(0);
        });

        Tag response = this.underTest.createTagIfNotExists("anyString");

        assertNotNull(response);
        assertEquals(1L, response.getAccessCount());
    }

    @Test
    @DisplayName("Should return Page<TagDTO> when receives Pageable in findAllOrderByAccessCount")
    void findAllOrderByAccessCount() {
        List<Tag> list = List.of(new Tag(1L, "tag1", 10L));

        when(this.tagRepository.findAllByOrderByAccessCountDesc(any(Pageable.class))).thenReturn(new PageImpl<>(list));

        Page<TagDTO> response = this.underTest.findAllOrderByAccessCount(Pageable.unpaged());

        assertEquals(list.get(0).getId(), response.getContent().get(0).getId());
    }

    @Test
    @DisplayName("Should return tag when save successfull")
    void save() {
        Tag expected = Tag.builder().build();
        Long idExpected = 1L;
        when(this.tagRepository.save(any(Tag.class))).then(iv -> {
            Tag tag = iv.getArgument(0);
            tag.setId(idExpected);
            return tag;
        });

        Tag response = this.underTest.save(new Tag());

        assertEquals(idExpected, response.getId());
    }
}
