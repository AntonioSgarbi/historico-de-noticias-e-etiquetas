package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import tech.antoniosgarbi.desafioapi.dto.*;
import tech.antoniosgarbi.desafioapi.exception.NewsNotFoundException;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.service.AccessTagRegisterService;
import tech.antoniosgarbi.desafioapi.service.UserCustomerService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class CustomerServiceImplTest {

    @Mock
    private IntegrationServiceImpl integrationService;
    @Mock
    private TagServiceImpl tagService;
    @Mock
    private UserCustomerService userCustomerService;
    @Mock
    private AccessTagRegisterService accessTagRegisterService;
    @InjectMocks
    private CustomerServiceImpl underTest;

    @Test
    @DisplayName("Should throws UsernameNotFoundException when UserDetails not found")
    void query1() {
        Tag tag = Tag.builder().id(1L).build();
        when(this.tagService.createTagIfNotExists(anyString())).thenReturn(tag);

        when(this.userCustomerService.findModelByEmail(anyString()))
                .thenThrow(new UsernameNotFoundException("Usuario não foi encontrado"));

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Principal principal = new PreAuthenticatedAuthenticationToken(null, null);

        assertThrows(UsernameNotFoundException.class, () -> this.underTest.query("query", "date", principal));
    }

    @Test
    @DisplayName("Should return List of NewsDTO when successfull")
    void query2() {
        Tag tag = Tag.builder().id(1L).build();
        when(this.tagService.createTagIfNotExists(anyString())).thenReturn(tag);

        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setId(1L);

        when(this.userCustomerService.findModelByEmail(anyString())).thenReturn(userCustomer);

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Principal principal = new PreAuthenticatedAuthenticationToken(null, null);

        NewsIntegrationDTO ni1  = NewsIntegrationDTO.builder().title("ni1").description("desc 1").link("link 1").build();
        NewsIntegrationDTO ni2  = NewsIntegrationDTO.builder().title("ni2").description("desc 2").link("link 2").build();
        NewsIntegrationDTO ni3  = NewsIntegrationDTO.builder().title("ni3").description("desc 3").link("link 3").build();

        IntegrationDTO integrationDTO = new IntegrationDTO();

        integrationDTO.setList(List.of(ni1, ni2, ni3));

        when(this.integrationService.query(anyString(), anyString())).thenReturn(integrationDTO);

        List<NewsDTO> response = this.underTest.query("query", "date", principal);

        assertEquals(ni1.getTitle(), response.get(0).getTitle());
        assertEquals(ni2.getTitle(), response.get(1).getTitle());
        assertEquals(ni3.getTitle(), response.get(2).getTitle());
    }

    @Test
    @DisplayName("Should throws NewsNotFoundException when response list is empty")
    void query3() {
        Tag tag = Tag.builder().id(1L).build();
        when(this.tagService.createTagIfNotExists(anyString())).thenReturn(tag);

        UserCustomer userCustomer = new UserCustomer();
        userCustomer.setId(1L);

        when(this.userCustomerService.findModelByEmail(anyString())).thenReturn(userCustomer);

        org.springframework.security.core.userdetails.User user = new org.springframework.security.core.userdetails.User("user", "pass", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Principal principal = new PreAuthenticatedAuthenticationToken(null, null);

        IntegrationDTO integrationDTO = new IntegrationDTO();
        integrationDTO.setList(new ArrayList<>());

        when(this.integrationService.query(anyString(), anyString())).thenReturn(integrationDTO);

        var e = assertThrows(NewsNotFoundException.class, () -> this.underTest.query("query", "date", principal));

        assertEquals("Não existem notícias para esta busca", e.getMessage());
    }

    @Test
    @DisplayName("Should return Page<AccessTagRegisterDTO> when receives principal and pageable")
    void getTagsHistory1() {
        AccessTagRegisterDTO tag = new AccessTagRegisterDTO(1L, "tag1", new Date());

        List<AccessTagRegisterDTO> list = List.of(tag);
        UserCustomer userCustomer = new UserCustomer();

        when(this.userCustomerService.findModelByEmail(anyString())).thenReturn(userCustomer);

        when(this.accessTagRegisterService.findTagsHistoryByUser(any(UserCustomer.class), any(Pageable.class)))
                .thenReturn(new PageImpl<AccessTagRegisterDTO>(list));

        UserDetails user = new User("username", "password", new ArrayList<>());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        Page<AccessTagRegisterDTO> response = this.underTest.getTagsHistory(principal, Pageable.unpaged());

        assertEquals(list, response.getContent());
    }

    @Test
    @DisplayName("Should return message \"tag adicionada\" when successfull")
    void addTag() {
        when(this.tagService.createTagIfNotExists(anyString())).thenReturn(new Tag());

        UserDetails user = new User("username", "password", new ArrayList<>());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        String response = this.underTest.addTag(principal, "tag");

        assertEquals("tag adicionada", response);
    }

    @Test
    void removeTag() {
        when(this.tagService.createTagIfNotExists(anyString())).thenReturn(new Tag());

        UserDetails user = new User("username", "password", new ArrayList<>());
        Principal principal = new UsernamePasswordAuthenticationToken(user, null);
        String response = this.underTest.removeTag(principal, "tag");

        assertEquals("tag removida", response);
    }
}