package ru.practicum.server.like.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "events_likes")
@IdClass(LikeInfoId.class)
public class LikeInfo {
    @Id
    @Column(name = "event_id")
    private Long eventId;

    @Id
    @Column(name = "estimater_id")
    private Long estimaterId;

    @Column(name = "positive")
    private Integer positive;
}
