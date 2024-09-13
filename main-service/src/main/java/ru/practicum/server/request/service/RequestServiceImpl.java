package ru.practicum.server.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.InvalidDataException;
import ru.practicum.server.exception.NotFoundException;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.RequestStatus;
import ru.practicum.server.request.model.dto.RequestDto;
import ru.practicum.server.request.model.dto.RequestStatusUpdate;
import ru.practicum.server.request.model.dto.RequestUpdateDto;
import ru.practicum.server.request.model.dto.mapper.RequestMapper;
import ru.practicum.server.request.repository.RequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public RequestDto create(long userId, long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);

        Request request = new Request(null, LocalDateTime.now(), event, requester, RequestStatus.PENDING);
        validateRequest(request);
        Request saved = requestRepository.save(request);
        RequestDto dto = RequestMapper.mapToRequestDto(saved);
        log.info("POST /users/{}/requests -> returning from db {}", userId, dto);
        return dto;
    }

    private void validateRequest(Request request) {
        requestRepository.findByEventIdAndRequesterId(request.getEvent().getId(), request.getRequester().getId()).ifPresent(value -> {
            throw new InvalidDataException("Вы уже отправили заявку на это событие!");
        });
        if (request.getEvent().getInitiator().getId().equals(request.getRequester().getId())) {
            throw new InvalidDataException("Вы не можете отправить заявку на участии в собственном событии!");
        }
        if (request.getEvent().getState() != EventState.PUBLISHED) {
            throw new InvalidDataException("Это событие еще не было опубликовано!");
        }
        if (request.getEvent().getParticipantLimit() != 0) {
            if (request.getEvent().getConfirmedRequests().equals(request.getEvent().getParticipantLimit())) {
                throw new InvalidDataException("Превышен лимит заявок на это событие!");
            }
        }
        if (request.getEvent().getParticipantLimit() == 0) {
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            request.setStatus(RequestStatus.CONFIRMED);
        }
        if (!request.getEvent().getRequestModeration()) {
            request.setStatus(RequestStatus.CONFIRMED);
            Event event = request.getEvent();
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestDto> getAll(long userId) {
        List<Request> requests = requestRepository.findAllByRequesterId(userId);
        List<RequestDto> dtos = requests.stream().map(RequestMapper::mapToRequestDto).toList();
        log.info("GET /users/{}/requests -> returning from db", userId);
        return dtos;
    }

    @Override
    @Transactional
    public RequestDto cancel(long userId, long requestId) {
        userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Request request = requestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        request.setStatus(RequestStatus.CANCELED);
        Request saved = requestRepository.save(request);
        Event event = request.getEvent();
        event.setConfirmedRequests(event.getConfirmedRequests() - 1);
        eventRepository.save(event);
        RequestDto dto = RequestMapper.mapToRequestDto(saved);
        log.info("PATCH /users/{}/requests/{}/cancel -> returning from db {}", userId, requestId, dto);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<RequestDto> getRequestsForEvent(long userId, long eventId) {
        eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        List<RequestDto> dtos = requests.stream().map(RequestMapper::mapToRequestDto).toList();
        log.info("GET /users/{}/events/{}/requests -> returning form db {}", userId, eventId, dtos);
        return dtos;
    }

    @Override
    @Transactional
    public RequestStatusUpdate confirmRequests(long userId, long eventId, RequestUpdateDto dto) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        List<Request> confirmedRequests = requestRepository.findAllByEventIdAndStatus(eventId, RequestStatus.CONFIRMED);
        List<Request> requests = requestRepository.findAllById(dto.getRequestIds());
        if (!requests.isEmpty()) {
            if (requests.stream().anyMatch(request -> request.getStatus() == RequestStatus.CONFIRMED)) {
                throw new InvalidDataException("Вы уже подтвердили заявку!");
            }
            requests = requests.stream().filter(request -> request.getStatus() == RequestStatus.PENDING).toList();
            if (!requests.isEmpty()) {
                if (dto.getStatus().equals("CONFIRMED")) {
                    requests = requests.stream().peek(request -> request.setStatus(RequestStatus.CONFIRMED)).toList();
                    for (Request request : requests) {
                        if (confirmedRequests.size() == request.getEvent().getParticipantLimit()) {
                            throw new InvalidDataException("Превышен лимит участников для этого события!");
                        } else {
                            request.setStatus(RequestStatus.CONFIRMED);
                            confirmedRequests.add(request);
                            event.setConfirmedRequests(confirmedRequests.size());
                        }
                    }
                } else if (dto.getStatus().equals("REJECTED")) {
                    requests = requests.stream().peek(request -> request.setStatus(RequestStatus.REJECTED)).toList();
                }
                List<Request> saved = requestRepository.saveAll(requests);
                eventRepository.save(event);
                RequestStatusUpdate requestStatusUpdate = null;
                if (dto.getStatus().equals("CONFIRMED")) {
                    List<RequestDto> requestDtos = saved.stream().map(RequestMapper::mapToRequestDto).toList();
                    requestStatusUpdate = new RequestStatusUpdate(requestDtos, List.of());
                }
                if (dto.getStatus().equals("REJECTED")) {
                    List<RequestDto> requestDtos = saved.stream().map(RequestMapper::mapToRequestDto).toList();
                    requestStatusUpdate = new RequestStatusUpdate(List.of(), requestDtos);
                }
                log.info("PATCH /users/{}/events/{}/requests -> returning from db {}", userId, eventId, requests);
                return requestStatusUpdate;
            }
        }
        eventRepository.save(event);
        log.info("PATCH /users/{}/events/{}/requests -> returning from db {}", userId, eventId, requests);
        return new RequestStatusUpdate();
    }
}
