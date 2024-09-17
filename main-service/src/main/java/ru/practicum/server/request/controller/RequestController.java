package ru.practicum.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.model.dto.RequestDto;
import ru.practicum.server.request.model.dto.RequestStatusUpdate;
import ru.practicum.server.request.model.dto.RequestUpdateDto;
import ru.practicum.server.request.service.RequestService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDto create(@PathVariable long userId, @RequestParam long eventId) {
        log.info("POST /users/{}/requests <- with eventId={}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public Collection<RequestDto> getAll(@PathVariable long userId) {
        log.info("GET /users/{}/requests <-", userId);
        return requestService.getAll(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public RequestDto cancel(@PathVariable long userId, @PathVariable long requestId) {
        log.info("PATCH /users/{}/requests/{}/cancel", userId, requestId);
        return requestService.cancel(userId, requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<RequestDto> getRequestsForEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info("GET /users/{}/events/{}/requests <-", userId, eventId);
        return requestService.getRequestsForEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestStatusUpdate confirmRequests(@PathVariable long userId, @PathVariable long eventId,
                                               @RequestBody RequestUpdateDto dto) {
        log.info("PATCH /users/{}/events/{}/requests <- {}", userId, eventId, dto);
        return requestService.confirmRequests(userId, eventId, dto);
    }
}
