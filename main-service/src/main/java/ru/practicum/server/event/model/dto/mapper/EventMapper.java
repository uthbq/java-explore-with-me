package ru.practicum.server.event.model.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.dto.EventDto;
import ru.practicum.server.event.model.dto.EventDtoPatch;

@UtilityClass
public class EventMapper {
    public static Event mapToEvent(EventDto dto) {
        return new Event(dto.getTitle(), dto.getAnnotation(), dto.getDescription(), dto.getEventDate(), dto.getLocation(), dto.getPaid(), dto.getParticipantLimit(), dto.getRequestModeration());
    }

    public static Event updateEvent(Event old, EventDtoPatch updated) {
        if (updated.getPaid() != null) {
            old.setPaid(updated.getPaid());
        }
        if (updated.getRequestModeration() != null) {
            old.setRequestModeration(updated.getRequestModeration());
        }
        if (updated.getEventDate() != null) {
            old.setEventDate(updated.getEventDate());
        }
        if (updated.getDescription() != null) {
            old.setDescription(updated.getDescription());
        }
        if (updated.getAnnotation() != null) {
            old.setAnnotation(updated.getAnnotation());
        }
        if (updated.getLocation() != null) {
            old.setLocation(updated.getLocation());
        }
        if (updated.getParticipantLimit() !=  null) {
            old.setParticipantLimit(updated.getParticipantLimit());
        }
        if (updated.getTitle() != null) {
            old.setTitle(updated.getTitle());
        }

        return old;
    }
}
