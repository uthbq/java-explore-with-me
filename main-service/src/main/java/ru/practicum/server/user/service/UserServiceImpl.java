package ru.practicum.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.model.dto.UserDto;
import ru.practicum.server.user.model.dto.UserMapper;
import ru.practicum.server.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(UserDto userDto) {
        User user = userRepository.save(UserMapper.mapToUser(userDto));
        log.info("POST /admin/users -> returning from db {}", user);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> get(Long[] ids, int from, int size) {
        Collection<User> users;
        if (ids != null) {
            users = userRepository.findAllById(List.of(ids));
        } else {
            Pageable pageable = PageRequest.of(from / size, size);
            users = userRepository.findAll(pageable).getContent();
        }
        log.info("GET /admin/users -> returning from db {}", users);
        return users;
    }

    @Override
    @Transactional
    public void delete(long userId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        userRepository.deleteById(userId);
        log.info("DELETE /admin/users/{} -> deleted from db", userId);
    }
}
