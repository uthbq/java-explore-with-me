package ru.practicum.server.user.model.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.server.user.model.User;

@UtilityClass
public class UserMapper {
    public static User mapToUser(UserDto userDto) {
        return new User(null, userDto.getEmail(), userDto.getName());
    }
}
