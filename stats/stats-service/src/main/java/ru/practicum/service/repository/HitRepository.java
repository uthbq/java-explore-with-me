package ru.practicum.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.service.model.EndpointHit;
import ru.practicum.models.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.models.dto.ViewStats(eh.app, eh.uri, count(eh.id)) FROM EndpointHit AS eh WHERE eh.timestamp BETWEEN ?1 AND ?2 AND eh.uri = ?3 GROUP BY eh.app, eh.uri")
    ViewStats getViewStats(LocalDateTime start, LocalDateTime end, String uri);

    @Query("SELECT new ru.practicum.models.dto.ViewStats(eh.app, eh.uri, count(DISTINCT eh.ip)) FROM EndpointHit AS eh WHERE eh.timestamp BETWEEN ?1 AND ?2 AND eh.uri = ?3 GROUP BY eh.app, eh.uri")
    ViewStats getViewStatsUnique(LocalDateTime start, LocalDateTime end, String uri);

    @Query("SELECT new ru.practicum.models.dto.ViewStats(eh.app, eh.uri, count(eh.id)) FROM EndpointHit AS eh WHERE eh.timestamp BETWEEN ?1 AND ?2 GROUP BY eh.app, eh.uri")
    List<ViewStats> getAllViewStats(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.models.dto.ViewStats(eh.app, eh.uri, count(DISTINCT eh.ip)) FROM EndpointHit AS eh WHERE eh.timestamp BETWEEN ?1 AND ?2 GROUP BY eh.app, eh.uri")
    List<ViewStats> getAllViewStatsUnique(LocalDateTime start, LocalDateTime end);
}
