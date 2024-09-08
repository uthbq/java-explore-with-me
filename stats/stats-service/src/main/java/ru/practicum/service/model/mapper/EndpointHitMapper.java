package ru.practicum.service.model.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.models.dto.EndpointHitDto;
import ru.practicum.service.model.EndpointHit;

@UtilityClass
public class EndpointHitMapper {
    public static EndpointHit mapToEndpointHit(EndpointHitDto dto) {
        return new EndpointHit(null, dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp());
    }

    public static EndpointHitDto maptoEndpointHitDto(EndpointHit hit) {
        return new EndpointHitDto(hit.getApp(), hit.getUri(), hit.getIp(), hit.getTimestamp());
    }
}
