package ru.practicum.server.request.model.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.server.request.model.Request;
import ru.practicum.server.request.model.dto.RequestDto;

@UtilityClass
public class RequestMapper {
    public static RequestDto mapToRequestDto(Request request) {
        return new RequestDto(request.getId(), request.getCreated(), request.getEvent().getId(), request.getRequester().getId(), request.getStatus());
    }
}
