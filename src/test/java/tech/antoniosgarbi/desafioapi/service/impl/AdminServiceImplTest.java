package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.antoniosgarbi.desafioapi.configuration.SecurityConfig;
import tech.antoniosgarbi.desafioapi.dto.*;
import tech.antoniosgarbi.desafioapi.exception.UserException;
import tech.antoniosgarbi.desafioapi.mapper.UserMapper;
import tech.antoniosgarbi.desafioapi.model.Tag;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserCustomerRepository;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;
import tech.antoniosgarbi.desafioapi.service.AccessTagRegisterService;
import tech.antoniosgarbi.desafioapi.service.GeneratorService;
import tech.antoniosgarbi.desafioapi.service.IntegrationService;
import tech.antoniosgarbi.desafioapi.service.TagService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminServiceImplTest {

    private static MockedStatic<SecurityConfig> securityConfig;
    private static MockedStatic<UserMapper> mapper;
    @Mock
    private GeneratorService generatorService;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private MailSpringServiceImpl mailSpringService;
    @Mock
    private AccessTagRegisterService accessTagRegisterService;
    @Mock
    private TagService tagService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCustomerRepository userCustomerRepository;
    @Mock
    private IntegrationService integrationService;
    @InjectMocks
    private AdminServiceImpl underTest;


    @BeforeEach
    void setUp() {
        securityConfig = mockStatic(SecurityConfig.class);
        mapper = mockStatic(UserMapper.class);
    }

    @AfterEach
    void tearDown() {
        securityConfig.close();
        mapper.close();
    }


    @Test
    @DisplayName("Should return UserDTO when create successfull")
    void create1() {
        User user = User.builder().email("email").role("USER").build();

        mapper.when(() -> UserMapper.toUser(any(UserDTO.class))).thenReturn(user);

        when(this.generatorService.generate(anyByte())).thenReturn("senhadedezeisdig");

        securityConfig.when(SecurityConfig::passwordEncoder).thenReturn(passwordEncoder);

        String expectedPassword = "encoded209182109281902819";
        when(passwordEncoder.encode(anyString())).thenReturn(expectedPassword);

        Long expectedId = 1L;
        when(this.userRepository.save(any(User.class))).then(iv -> {
            User u = iv.getArgument(0);
            u.setId(expectedId);
            return u;
        });

        UserDTO response = this.underTest.create(UserDTO.builder().id(1L).role("USER").email("email").build());

        assertEquals(expectedId, response.getId());
    }

    @Test
    @DisplayName("Should return UserModel when findById successfull")
    void findById1() {
        User expected = User.builder().id(1L).email("email").password("pass").role("USER").build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        User response = this.underTest.findById(1L);

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should throws UsernameNotFoundException when findById returns empty optional")
    void findById2() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.underTest.findById(1L));
    }


    @Test
    @DisplayName("Should throws UsernameNotFoundExcetion when receives user id that returns empty optional")
    void findTagsHistoryByUser1() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                this.underTest.findTagsHistoryByUser(1L, Pageable.unpaged()));
    }

    @Test
    @DisplayName("Should throws UserExcetion when receives user id that returns optional with wrong user class")
    void findTagsHistoryByUser2() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () ->
                this.underTest.findTagsHistoryByUser(1L, Pageable.unpaged()));
    }

    @Test
    @DisplayName("Should return Page<AccessTagRegisterDTO when successfull")
    void findTagsHistoryByUser3() {
        User user = new UserCustomer();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        Page<AccessTagRegisterDTO> expected = Page.empty();
        when(this.accessTagRegisterService.findTagsHistoryByUser(any(UserCustomer.class), any(Pageable.class)))
                .thenReturn(expected);

        Page<AccessTagRegisterDTO> response = this.underTest.findTagsHistoryByUser(1L, Pageable.unpaged());

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should return Page<TagDTO when successfull")
    void findAllTagsOrderByAccessCount() {
        Page<TagDTO> expected = Page.empty();
        when(this.tagService.findAllOrderByAccessCount(any(Pageable.class))).thenReturn(expected);

        Page<TagDTO> response = this.underTest.findAllTagsOrderByAccessCount(Pageable.unpaged());

        assertEquals(expected, response);
    }

    @Test
    @DisplayName("Should send mail when has news from day")
    void sendNewsToUsers1() {
        String date = "30/30/2022";
        when(this.integrationService.getDateToday()).thenReturn(date);

        Tag tag1 = Tag.builder().id(1L).tag("tag1").accessCount(10L).build();
        Tag tag2 = Tag.builder().id(2L).tag("tag2").accessCount(20L).build();

        UserCustomer user1  = new UserCustomer();
        user1.setId(1L);
        user1.setRegisteredTags(Set.of(tag1));

        UserCustomer user2  = new UserCustomer();
        user2.setId(2L);
        user2.setRegisteredTags(Set.of(tag1, tag2));

        when(this.userCustomerRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)));

        when(this.tagService.save(any(Tag.class)))
                .thenReturn(tag1)
                .thenReturn(tag1)
                .thenReturn(tag2);
        IntegrationDTO integrationDTO = new IntegrationDTO();
        NewsIntegrationDTO news = new NewsIntegrationDTO();
        news.setDate(date);
        integrationDTO.setList(List.of(news));

        when(this.integrationService.query(anyString(), anyString()))
                .thenReturn(integrationDTO)
                .thenReturn(integrationDTO)
                .thenReturn(integrationDTO);


        this.underTest.sendNewsToUsers();

        verify(this.mailSpringService, atLeast(2)).sendText(any(), any(), any());
    }
    @Test
    @DisplayName("Should send email with different message when there is no news of the day")
    void sendNewsToUsers2() {
        String date = "30/30/2022";
        when(this.integrationService.getDateToday()).thenReturn(date);

        Tag tag1 = Tag.builder().id(1L).tag("tag1").accessCount(10L).build();
        Tag tag2 = Tag.builder().id(2L).tag("tag2").accessCount(20L).build();

        UserCustomer user1  = new UserCustomer();
        user1.setId(1L);
        user1.setRegisteredTags(Set.of(tag1));

        UserCustomer user2  = new UserCustomer();
        user2.setId(2L);
        user2.setRegisteredTags(Set.of(tag1, tag2));

        when(this.userCustomerRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)))
                .thenReturn(new PageImpl<>(List.of(user1, user2)));

        when(this.tagService.save(any(Tag.class)))
                .thenReturn(tag1)
                .thenReturn(tag1)
                .thenReturn(tag2);
        IntegrationDTO integrationDTO = new IntegrationDTO();
        NewsIntegrationDTO news = new NewsIntegrationDTO();
        news.setDate("any date");
        integrationDTO.setList(List.of(news));

        when(this.integrationService.query(anyString(), anyString()))
                .thenReturn(integrationDTO)
                .thenReturn(integrationDTO)
                .thenReturn(integrationDTO);


        this.underTest.sendNewsToUsers();

        verify(this.mailSpringService, atLeast(2)).sendText(any(), any(), any());
    }

    @Test
    @DisplayName("Should delete user")
    void delete1() {
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));

        this.underTest.delete(1L);

        verify(this.userRepository).delete(any(User.class));
    }

    @Test
    @DisplayName("Should delete history before delete user")
    void delete2() {
        when(this.userRepository.findById(anyLong())).thenReturn(Optional.of(new UserCustomer()));

        this.underTest.delete(1L);

        verify(this.accessTagRegisterService).deleteHistoryFromUser(any(UserCustomer.class));
        verify(this.userRepository).delete(any(User.class));
    }

    @Test
    @DisplayName("Should return Page<UserDTO>")
    void listUsers1() {
        when(this.userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(new User())));

        UserDTO dto = new UserDTO(1L,"email", "role");

        mapper.when(() -> UserMapper.toDTO(any(User.class))).thenReturn(dto);

        Page<UserDTO> response = this.underTest.listUsers(Pageable.unpaged());

        assertEquals(dto.getId(), response.getContent().get(0).getId());
        assertEquals(dto.getEmail(), response.getContent().get(0).getEmail());
        assertEquals(dto.getRole(), response.getContent().get(0).getRole());
    }

}
