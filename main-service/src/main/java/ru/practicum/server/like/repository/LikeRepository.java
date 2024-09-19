package ru.practicum.server.like.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.server.like.model.LikeInfo;
import ru.practicum.server.like.model.LikeInfoId;

public interface LikeRepository extends JpaRepository<LikeInfo, LikeInfoId> {
    @Query(value = "SELECT SUM(positive) " +
                    "FROM LikeInfo " +
                    "WHERE eventId = ?1")
    Integer findRatingByEventId(Long eventId);

    @Query(value = "SELECT eventId, SUM(positive) FROM LikeInfo WHERE eventId IN :eventIds GROUP BY eventId")
    Map<Long, Integer> findRatingsByEventIds(List<Long> eventIds);

    @Query("SELECT new ru.practicum.server.like.model.LikeInfo(eventId, SUM(positive)) " +
            "FROM LikeInfo WHERE eventId IN :eventIds GROUP BY eventId")
    List<LikeInfo> findRatingsByEventIds(List<Long> eventIds);

}
