package com.example.dailymoodtracker.mapper;

import com.example.dailymoodtracker.dto.UserDto;
import com.example.dailymoodtracker.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        return new UserDto(
            user.getId(),
            user.getUsername(),
            user.getEmail()
        );
    }

    public User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.id());
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        return user;
    }
}