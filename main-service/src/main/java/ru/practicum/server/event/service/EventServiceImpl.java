package ru.practicum.server.event.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.models.dto.EndpointHitDto;
import ru.practicum.server.category.model.Category;
import ru.practicum.server.category.repository.CategoryRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.model.dto.EventDto;
import ru.practicum.server.event.model.dto.EventDtoPatch;
import ru.practicum.server.event.model.dto.EventSearch;
import ru.practicum.server.event.model.dto.mapper.EventMapper;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.IncorrectDateException;
import ru.practicum.server.exception.InvalidDataException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final StatsClient statsClient;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Event create(long userId, EventDto eventDto) {
        User initiator = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(NotFoundException::new);
        Event event = EventMapper.mapToEvent(eventDto);
        event.setInitiator(initiator);
        event.setCategory(category);
        setDefaultValuesOnCreate(event);
        checkCorrectEventDate(event.getEventDate());
        Event saved = eventRepository.save(event);
        log.info("POST /users/{}/events -> returning from db {}", userId, saved);
        return eventRepository.save(saved);
    }

    private void setDefaultValuesOnCreate(Event event) {
        event.setViews(0);
        event.setConfirmedRequests(0);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);
    }

    private void checkCorrectEventDate(LocalDateTime date) {
        if (date != null) {
            if (date.minusHours(2L).isBefore(LocalDateTime.now())) {
                throw new IncorrectDateException("Ваше событие начинается раньше, чем через два часа!");
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Event get(long userId, long eventId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        log.info("GET /users/{}/events/{} -> returning from db {}", userId, eventId, event);
        return event;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> getAll(long userId, int from, int size) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageable);
        log.info("GET /users/{}/events ->", userId);
        return events;
    }

    @Override
    @Transactional
    public Event update(long userId, long eventId, EventDtoPatch eventDto) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event old = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (old.getState() == EventState.PUBLISHED) {
            throw new InvalidDataException("Вы уже не можете изменить это событие!");
        }
        if (eventDto.getDescription() != null) {
            if (eventDto.getDescription().isBlank()) {
                throw new InvalidDataException("Вы неверно ввели описание события");
            }
        }
        if (eventDto.getAnnotation() != null) {
            if (eventDto.getAnnotation().isBlank()) {
                throw new InvalidDataException("Вы неверно ввели описание события");
            }
        }
        checkCorrectEventDate(eventDto.getEventDate());
        if (eventDto.getCategory() != null) {
            Category category = categoryRepository.findById(eventDto.getCategory()).orElseThrow(NotFoundException::new);
            old.setCategory(category);
        }
        Event event = EventMapper.updateEvent(old, eventDto);
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState(EventState.CANCELED);
            }
            if (eventDto.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState(EventState.PENDING);
            }
        }
        Event saved = eventRepository.save(EventMapper.updateEvent(old, eventDto));
        log.info("PATCH /users/{}/events/{} -> returning from db {}", userId, eventId, saved);
        return saved;
    }

    @Override
    @Transactional
    public Event adminUpdate(long eventId, EventDtoPatch eventDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (event.getEventDate().minusHours(1L).isBefore(LocalDateTime.now())) {
            throw new InvalidDataException("Ваше событие начинается раньше, чем через час!");
        }
        if (event.getState() != EventState.PENDING) {
            throw new InvalidDataException("Это событие уже нельзя обновить!");
        }
        if (eventDto.getDescription() != null) {
            if (eventDto.getDescription().isBlank()) {
                throw new InvalidDataException("Вы неверно ввели описание события");
            }
        }
        if (eventDto.getAnnotation() != null) {
            if (eventDto.getAnnotation().isBlank()) {
                throw new InvalidDataException("Вы неверно ввели описание события");
            }
        }

        Event updated = EventMapper.updateEvent(event, eventDto);
        if (eventDto.getStateAction() != null) {
            if (eventDto.getStateAction().equals("PUBLISH_EVENT")) {
                if (event.getState() == EventState.CANCELED) {
                    throw new InvalidDataException("Это событие было отклонено!");
                }
                updated.setState(EventState.PUBLISHED);
                updated.setPublishedOn(LocalDateTime.now());
            } else if (eventDto.getStateAction().equals("REJECT_EVENT")) {
                updated.setState(EventState.CANCELED);
            }
        }
        Event saved = eventRepository.save(updated);
        log.info("PATCH /admin/events/{} -> returning from db {}", eventId, saved);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> adminGet(Long[] users, EventState[] states, Long[] categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        if (states == null) {
            states = EventState.values();
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100);
        }
        List<Event> events = eventRepository.adminGet(users, states, categories, rangeStart, rangeEnd, pageable);
        log.info("GET /admin/events -> returning from db");
        return events;
    }

    @Override
    @Transactional(readOnly = true)
    public Event publicGet(long eventId, HttpServletRequest servletRequest) {
        statsClient.postHit(new EndpointHitDto("ewm-main-service", servletRequest.getRequestURI(), servletRequest.getRemoteAddr(), LocalDateTime.now()));
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с таким id не найдено!");
        }
        event.setViews(event.getViews() + 1);
        eventRepository.save(event);
        log.info("/GET /events/{} -> returning from db {}", eventId, event);
        return event;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Event> publicGetSorted(String text, Long[] categories, Boolean paid, LocalDateTime rangeStart,
                                             LocalDateTime rangeEnd, Boolean onlyAvailable, EventSearch sort,
                                             int from, int size, HttpServletRequest servletRequest) {
        statsClient.postHit(new EndpointHitDto("ewm-main-service", servletRequest.getRequestURI(), servletRequest.getRemoteAddr(), LocalDateTime.now()));
        String sortBy;
        switch (sort) {
            case EVENT_DATE -> sortBy = "eventDate";
            case VIEWS -> sortBy = "views";
            default -> sortBy = "id";
        }
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }
        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(10);
        }
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sortBy).descending());
        List<Event> events;
        if (onlyAvailable) {
            events = eventRepository.getAvailableEventsFiltered(text, categories, paid, rangeStart, rangeEnd, pageable);
        } else {
            events = eventRepository.getEventsFiltered(text, categories, paid, rangeStart, rangeEnd, pageable);
        }
        log.info("GET /events -> returning from db");
        return events;
    }
}
