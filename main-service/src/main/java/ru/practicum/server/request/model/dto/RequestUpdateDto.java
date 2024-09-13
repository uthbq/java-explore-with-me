package ru.practicum.server.request.model.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestUpdateDto {
    private List<Long> requestIds;
    private String status;
}
