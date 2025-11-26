package org.example.smartshop.model.mapper;

import org.example.smartshop.model.dto.UserDto;
import org.example.smartshop.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto userDto);
}
