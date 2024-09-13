package ru.practicum.server.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(long userId, Pageable pageable);

    @Query(value = "select e from Event as e " +
                    "where ?1 is null or e.initiator.id in ?1 " +
                    "and ?2 is null or e.state in ?2 " +
                    "and ?3 is null or e.category.id in ?3 " +
                    "and e.eventDate between ?4 and ?5")
    List<Event> adminGet(Long[] users, EventState[] states, Long[] categories, LocalDateTime rangeStart,
                         LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where (lower(e.annotation) LIKE lower(concat('%',?1,'%')) " +
            "OR lower(e.description) LIKE lower(CONCAT('%',?1,'%')) OR ?1 is null) " +
            "and e.category.id in (?2) or ?2 is null " +
            "AND ?3 is null or e.paid = ?3 " +
            "AND e.eventDate between ?4 and ?5")
    List<Event> getEventsFiltered(String text, Long[] categories, Boolean paid,
                                LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where (lower(e.annotation) LIKE lower(concat('%',?1,'%')) " +
            "OR lower(e.description) LIKE lower(CONCAT('%',?1,'%')) OR ?1 is null) " +
            "and e.category.id in (?2) or ?2 is null " +
            "AND ?3 is null or e.paid = ?3 " +
            "AND e.eventDate between ?4 and ?5 " +
            "AND e.confirmedRequests < e.participantLimit")
    List<Event> getAvailableEventsFiltered(String text, Long[] categories, Boolean paid,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);
}
