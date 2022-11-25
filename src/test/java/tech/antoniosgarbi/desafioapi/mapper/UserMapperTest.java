package tech.antoniosgarbi.desafioapi.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
class UserMapperTest {

    @Test
    @DisplayName("Should return User when receives DTO with role admin")
    void toUser1() {
        UserDTO dto = new UserDTO(1L, "email", "ADMIN");

        User user = UserMapper.toUser(dto);

        assertInstanceOf(User.class, user);
    }

    @Test
    @DisplayName("Should return UserCustomer when receives DTO with role user")
    void toUser() {
        UserDTO dto = new UserDTO(1L, "email", "USER");

        User user = UserMapper.toUser(dto);

        assertInstanceOf(UserCustomer.class, user);
    }
}