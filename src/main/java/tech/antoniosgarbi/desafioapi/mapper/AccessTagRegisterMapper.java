package tech.antoniosgarbi.desafioapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import tech.antoniosgarbi.desafioapi.dto.AccessTagRegisterDTO;
import tech.antoniosgarbi.desafioapi.model.AccessTagRegister;

@Mapper
public abstract class AccessTagRegisterMapper {
    public static final AccessTagRegisterMapper INSTANCE = Mappers.getMapper(AccessTagRegisterMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "tag", expression = "java(model.getTag().getValue())"),
            @Mapping(target = "date", source = "date")})
    public abstract AccessTagRegisterDTO toDTO(AccessTagRegister model);
}
