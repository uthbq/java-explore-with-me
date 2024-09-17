package ru.practicum.server.exception;

import lombok.Data;

import java.time.Instant;

@Data
public class ErrorBody {
    private final String message;
    private final Instant instant = Instant.now();
}
