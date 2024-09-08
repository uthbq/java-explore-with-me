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
        List<ViewStats> viewStatsList = new ArrayList<>();
        if (uris == null) {
            if (unique) {
                viewStatsList = repository.getAllViewStats(start, end);
            } else {
                viewStatsList = repository.getAllViewStatsUnique(start, end);
            }
        } else {
            if (unique) {
                for (String s : uris) {
                    ViewStats viewStats = repository.getViewStatsUnique(start, end, s);
                    viewStatsList.add(Objects.requireNonNullElseGet(viewStats, () -> new ViewStats("ewm-main-service", s, 0L)));
                }
            } else {
                for (String s : uris) {
                    ViewStats viewStats = repository.getViewStats(start, end, s);
                    viewStatsList.add(Objects.requireNonNullElseGet(viewStats, () -> new ViewStats("ewm-main-service", s, 0L)));
                }
            }
        }
        viewStatsList.sort(Comparator.comparing(ViewStats::getHits).reversed());
        log.info("GET /stats -> returning from db {}", viewStatsList);
        return viewStatsList;
    }
}
