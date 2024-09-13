package ru.practicum.server.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestStatusUpdate {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}
