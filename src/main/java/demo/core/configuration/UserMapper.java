package demo.core.configuration;

import demo.DTO.UserSignupDto;
import demo.DTO.UserDto;
import demo.Model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserEntity user);

    @Mapping(target = "password", ignore = true)
    UserEntity signUpToUser(UserSignupDto signUpDto);

}
