package ru.practicum.server.event.model.dto;

import jakarta.persistence.Embeddable;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Embeddable
public class Location {
    private double lat;
    private double lon;
}
