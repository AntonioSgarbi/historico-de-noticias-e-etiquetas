package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tech.antoniosgarbi.desafioapi.exception.TagNotExistsException;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserCustomerRepository;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserCustomerServiceImplTest {

    @Mock
    private UserCustomerRepository userCustomerRepository;
    @InjectMocks
    private UserCustomerServiceImpl underTest;


    @Test
    @DisplayName("Should throws EntityNotFoundException when findByEmail returns empty optional")
    void findModelByEmail1() {
        when(userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.underTest.findModelByEmail("username"));
    }

    @Test
    @DisplayName("Should return UserModel when findModelByEmail successfull")
    void findModelByEmail2() {
        UserCustomer expected = new UserCustomer();
        when(userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(expected));

        User response = this.underTest.findModelByEmail("username");

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws EntityNotFoundException when findByEmail returns empty optional when addTagToUser")
    void addTagToUser1() {
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.underTest.addTagToUser(new Tag(), "username"));
    }

    @Test
    @DisplayName("Should add tag to favorites and save user when user favorites not contains added tag")
    void addTagToUser2() {
        Tag tag = new Tag();
        tag.setId(1L);

        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setRegisteredTags(Set.of(tag));
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(userCustomer));

        this.underTest.addTagToUser(new Tag(), "username");

        verify(this.userCustomerRepository).save(any());
    }

    @Test
    @DisplayName("Should do nothing when added tag is already in favorites")
    void addTagToUser3() {
        Tag tag = new Tag();
        tag.setId(1L);

        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setRegisteredTags(Set.of(tag));
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(userCustomer));


        assertDoesNotThrow(() -> this.underTest.addTagToUser(tag, "username"));
    }

    @Test
    @DisplayName("Should remove tag from favorites and set new collection to user")
    void removeTagFromUser1() {
        Tag tag = new Tag();
        tag.setId(1L);

        UserCustomer userCustomer = spy(new UserCustomer());
        userCustomer.setRegisteredTags(Set.of(tag));
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(userCustomer));

        this.underTest.removeTagFromUser(tag, "username");

        verify(userCustomer, atLeast(2)).setRegisteredTags(any());
    }

    @Test
    @DisplayName("Should save user when remove tag from favorites")
    void removeTagFromUser2() {
        Tag tag = new Tag();
        tag.setId(1L);

        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setRegisteredTags(Set.of(tag));
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(userCustomer));

        this.underTest.removeTagFromUser(tag, "username");

        verify(this.userCustomerRepository).save(any(UserCustomer.class));
    }

    @Test
    @DisplayName("Should throws EntityNotFoundException when findByEmail returns empty optional when removeTagFromUser")
    void removeTagFromUser3() {
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> this.underTest.removeTagFromUser(new Tag(), "username"));
    }

    @Test
    @DisplayName("Should throws TagNotExistsException when favorites doesnt contains removed tag removeTagFromUser")
    void removeTagFromUser4() {
        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setRegisteredTags(new HashSet<>());
        when(this.userCustomerRepository.findByEmail(anyString())).thenReturn(Optional.of(userCustomer));

        assertThrows(TagNotExistsException.class, () -> this.underTest.removeTagFromUser(new Tag(), "username"));
    }
}