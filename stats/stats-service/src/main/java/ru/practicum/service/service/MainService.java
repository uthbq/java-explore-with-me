package ru.practicum.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.models.dto.EndpointHitDto;
import ru.practicum.models.dto.ViewStats;
import ru.practicum.service.model.mapper.EndpointHitMapper;
import ru.practicum.service.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {
    private final HitRepository repository;

    @Transactional
    public EndpointHitDto saveHit(EndpointHitDto dto) {
        EndpointHitDto hit = EndpointHitMapper.maptoEndpointHitDto(repository.save(EndpointHitMapper.mapToEndpointHit(dto)));
        log.info("POST /hit -> returning from db {}", hit);
        return hit;
    }

    @Transactional(readOnly = true)
    public List<ViewStats> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        List<ViewStats> viewStatsList;

        if (uris == null) {
            // Если URI не указаны, получаем все статистики.
            if (unique) {
                viewStatsList = repository.getAllViewStatsUnique(start, end);
            } else {
                viewStatsList = repository.getAllViewStats(start, end);
            }
        } else {
            List<String> uriList = Arrays.asList(uris);
            if (unique) {
                viewStatsList = repository.getViewStatsForUrisUnique(start, end, uriList);
            } else {
                viewStatsList = repository.getViewStatsForUris(start, end, uriList);
            }
        }
        viewStatsList.sort(Comparator.comparing(ViewStats::getHits).reversed());

        log.info("GET /stats -> returning from db {}", viewStatsList);
        return viewStatsList;
    }

}

