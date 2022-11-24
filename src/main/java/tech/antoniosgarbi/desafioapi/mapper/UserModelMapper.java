package tech.antoniosgarbi.desafioapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import tech.antoniosgarbi.desafioapi.dto.UserDTO;
import tech.antoniosgarbi.desafioapi.model.User;

@Mapper(componentModel = "spring")
public abstract class UserModelMapper {
    public static final UserModelMapper INSTANCE = Mappers.getMapper(UserModelMapper.class);

    public abstract User toUser(UserDTO userDTO);

}
