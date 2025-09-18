package com.mleoni.demo_park_api.web.controller.dto.mapper;

import com.mleoni.demo_park_api.web.controller.dto.UserCreateDTO;
import com.mleoni.demo_park_api.web.controller.dto.UserResponseDTO;
import com.mleoni.demo_park_api.entities.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserCreateDTO createDto) {
        return new ModelMapper().map(createDto, User.class);
    }

    public static UserResponseDTO toDto(User user) {
        String role = user.getRole().name().substring("ROLE_".length());
        PropertyMap<User, UserResponseDTO> props = new PropertyMap<User, UserResponseDTO>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(user, UserResponseDTO.class);
    }

    public static List<UserResponseDTO> toListDto(List<User> users) {
        return users.stream().map(user -> toDto(user)).collect(Collectors.toList());
    }
}
