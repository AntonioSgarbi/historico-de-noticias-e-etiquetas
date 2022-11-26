package tech.antoniosgarbi.desafioapi.mapper;

import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.model.User;
import tech.antoniosgarbi.desafioapi.model.UserCustomer;

public class UserMapper {

    private UserMapper() {}

    public static User toUser(UserDTO userDTO) {
        User user;

        if(userDTO.getRole().toUpperCase().equals("ADMIN")) {
            user = new User();
        } else {
            user = new UserCustomer();
        }
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());

        return user;
    }

    public static UserDTO toDTO(User user) {
        UserDTO dto  = new UserDTO();

        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());

        return dto;
    }

}
