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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.antoniosgarbi.desafioapi.configuration.SecurityConfig;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.dto.TagDTO;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.exception.UserException;
import tech.antoniosgarbi.desafioapi.mapper.UserModelMapper;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;
import tech.antoniosgarbi.desafioapi.service.AccessTagRegisterService;
import tech.antoniosgarbi.desafioapi.service.GeneratorService;
import tech.antoniosgarbi.desafioapi.service.TagService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
class AdminServiceImplTest {

    private static MockedStatic<SecurityConfig> securityConfig;
    @Mock
    private static UserModelMapper mapper;
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
    @InjectMocks
    private AdminServiceImpl underTest;

    @BeforeEach
    void setUp() {
        securityConfig = mockStatic(SecurityConfig.class);
    }

    @AfterEach
    void tearDown() {
        securityConfig.close();
    }


    @Test
    @DisplayName("Should return UserDTO when create successfull")
    void create() {
        User user = User.builder().email("email").role("USER").build();

        when(mapper.toUser(any(UserDTO.class))).thenReturn(user);

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
}