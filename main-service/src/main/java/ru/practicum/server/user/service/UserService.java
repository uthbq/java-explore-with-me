package ru.practicum.server.user.service;

import ru.practicum.server.user.model.User;
import ru.practicum.server.user.model.dto.UserDto;

import java.util.Collection;

public interface UserService {
     User create(UserDto userDto);

    Collection<User> get(Long[] ids, int from, int size);

    void delete(long userId);
}
