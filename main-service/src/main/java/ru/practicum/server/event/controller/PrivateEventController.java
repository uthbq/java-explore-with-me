package ru.practicum.server.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.dto.EventDto;
import ru.practicum.server.event.model.dto.EventDtoPatch;
import ru.practicum.server.event.service.EventService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@PathVariable long userId, @RequestBody @Valid EventDto eventDto) {
        log.info("POST /users/{}/events <- {}", userId, eventDto);
        return eventService.create(userId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public Event get(@PathVariable long eventId, @PathVariable long userId) {
        log.info("GET /users/{}/events/{} <-", userId, eventId);
        return eventService.get(userId, eventId);
    }

    @GetMapping("/{userId}/events")
    public Collection<Event> getAll(@PathVariable long userId, @RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size) {
        log.info("GET /users/{}/events <-", userId);
        return eventService.getAll(userId, from, size);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public Event patch(@PathVariable long userId, @PathVariable long eventId, @RequestBody @Valid EventDtoPatch eventDto) {
        log.info("PATCH /users/{}/events/{} <- {}", userId, eventId, eventDto);
        return eventService.update(userId, eventId, eventDto);
    }
}
