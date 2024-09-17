package ru.practicum.server.event.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.model.dto.EventDtoPatch;
import ru.practicum.server.event.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
@Slf4j
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public Event patch(@PathVariable long eventId, @RequestBody @Valid EventDtoPatch eventDto) {
        log.info("PATCH /admin/events/{} <- {}", eventId, eventDto);
        return eventService.adminUpdate(eventId, eventDto);
    }

    @GetMapping
    public Collection<Event> get(@RequestParam(required = false) Long[] users, @RequestParam(required = false) EventState[] states, @RequestParam(required = false) Long[] categories,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                 @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                 @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /admin/events <-");
        return eventService.adminGet(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
