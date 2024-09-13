package ru.practicum.server.compilation.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.dto.CompilationDto;
import ru.practicum.server.compilation.model.dto.CompilationDtoPatch;
import ru.practicum.server.compilation.service.CompilationService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Compilation post(@RequestBody @Valid CompilationDto compDto) {
        log.info("POST /admin/compilations <- {}", compDto);
        return compilationService.create(compDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info("DELETE /admin/compilations/{} <-", compId);
        compilationService.delete(compId);
    }

    @PatchMapping("/{compId}")
    public Compilation patch(@PathVariable long compId, @RequestBody @Valid CompilationDtoPatch compDto) {
        log.info("PATCH /admin/compilations/{}", compId);
        return compilationService.update(compId, compDto);
    }
}
