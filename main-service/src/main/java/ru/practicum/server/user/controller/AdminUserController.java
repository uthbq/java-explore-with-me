package ru.practicum.server.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.model.dto.UserDto;
import ru.practicum.server.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class AdminUserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody @Valid UserDto userDto) {
        log.info("POST /admin/users <- {}", userDto);
        return userService.create(userDto);
    }

    @GetMapping
    public Collection<User> get(@RequestParam(required = false) Long[] ids, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/users <- {} with ids={}, from=, size={}", ids, from, size);
        return userService.get(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info("DELETE /admin/users/{} <-", userId);
        userService.delete(userId);
    }
}
