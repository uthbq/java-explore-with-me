package ru.practicum.server.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.service.CompilationService;

import java.util.Collection;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public Compilation get(@PathVariable long compId) {
        log.info("GET /compilations/{} <-", compId);
        return compilationService.get(compId);
    }

    @GetMapping
    public Collection<Compilation> getFiltered(@RequestParam(defaultValue = "false") boolean pinned, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("GET /compilations <-");
        return compilationService.getFiltered(pinned, from, size);
    }
}

