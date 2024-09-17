package ru.practicum.server.event.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.model.dto.EventDto;
import ru.practicum.server.event.model.dto.EventDtoPatch;
import ru.practicum.server.event.model.dto.EventSearch;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Event create(long userId, EventDto eventDto);

    Event get(long userId, long eventId);

    Collection<Event> getAll(long userId, int from, int size);

    Event update(long userId, long eventId, EventDtoPatch eventDto);

    Event adminUpdate(long eventId, EventDtoPatch eventDto);

    Collection<Event> adminGet(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, int from, int size);

    Event publicGet(long eventId, HttpServletRequest servletRequest);

    Collection<Event> publicGetSorted(String text, Long[] categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      Boolean onlyAvailable, EventSearch sort, int from, int size,
                                      HttpServletRequest servletRequest);

    Event like(long userId, long eventId, long likerId);

    void removeLike(long userId, long eventId, long likerId);

    Event dislike(long userId, long eventId, long dislikerId);

    void removeDislike(long userId, long eventId, long dislikerId);
}
