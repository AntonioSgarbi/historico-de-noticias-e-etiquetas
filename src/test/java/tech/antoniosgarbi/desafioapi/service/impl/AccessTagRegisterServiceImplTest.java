package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.AccessTagRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccessTagRegisterServiceImplTest {

    @Mock
    private AccessTagRepository accessTagRepository;
    @InjectMocks
    private AccessTagRegisterServiceImpl underTest;


    @Test
    @DisplayName("Should return Page of AccessTagRegisterDTO when receives valid user")
    void findTagsHistoryByUserId2() {
        Tag tag1 = Tag.builder().id(1L).build();
        Tag tag2 = Tag.builder().id(2L).build();
        Tag tag3 = Tag.builder().id(3L).build();

        UserCustomer user = new UserCustomer();
        user.setId(1L);

        AccessTagRegister atr1 = new AccessTagRegister(tag1, user);
        AccessTagRegister atr2 = new AccessTagRegister(tag2, user);
        AccessTagRegister atr3 = new AccessTagRegister(tag3, user);


        Page<AccessTagRegister> mockEntityPage = new PageImpl<>(List.of(atr1, atr2, atr3));

        when(this.accessTagRepository.findAllByUser(any(UserCustomer.class), any(Pageable.class))).thenReturn(mockEntityPage);

        Page<AccessTagRegisterDTO> pageResponse = this.underTest.findTagsHistoryByUser(user, Pageable.unpaged());

        assertEquals(mockEntityPage.getTotalElements(), pageResponse.getTotalElements());
    }

    @Test
    @DisplayName("Should save entity")
    void save1() {
        this.underTest.save(new AccessTagRegister());

        verify(this.accessTagRepository).save(any(AccessTagRegister.class));
    }

    @Test
    @DisplayName("Should delete all when receives user")
    void deleteHistoryFromUser() {
        this.underTest.deleteHistoryFromUser(new UserCustomer());

        verify(this.accessTagRepository).deleteAllByUser(any());
    }
}
