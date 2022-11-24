package tech.antoniosgarbi.desafioapi.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import tech.antoniosgarbi.desafioapi.configuration.SecurityConfig;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserDetailsServiceImplTest {

    private static MockedStatic<SecurityConfig> securityConfig;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl underTest;

    @BeforeEach
    void setUp() {
        securityConfig = mockStatic(SecurityConfig.class);
    }

    @AfterEach
    void tearDown() {
        securityConfig.close();
    }


    @Test
    @DisplayName("Should return UserDetails when successfull")
    void loadUserByUsername1() {
        User user = User.builder().id(1L).email("email").password("pass").role("USER").build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails response = this.underTest.loadUserByUsername("username");

        assertEquals(response.getUsername(), user.getEmail());
        assertEquals(response.getPassword(), user.getPassword());
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when findByEmail returns empty optional")
    void loadUserByUsername2() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.underTest.loadUserByUsername("username"));
    }

    @Test
    @DisplayName("Should throws when findById returns empty optional")
    void findById2() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> this.underTest.loadUserByUsername("username"));
    }

}