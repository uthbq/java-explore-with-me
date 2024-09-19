package ru.practicum.server.like.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.practicum.server.like.model.LikeInfo;

import java.util.List;
import java.util.Map;

public interface LikeRepository extends CrudRepository<LikeInfo, Long> {

    @Query("SELECT SUM(l.positive) FROM LikeInfo l WHERE l.eventId = :eventId")
    Integer findRatingByEventId(Long eventId);

    @Query("SELECT l.eventId, SUM(l.positive) FROM LikeInfo l WHERE l.eventId IN :eventIds GROUP BY l.eventId")
    Map<Long, Integer> findRatingsByEventIds(List<Long> eventIds);
}

