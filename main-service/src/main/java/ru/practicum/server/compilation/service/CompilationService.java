package ru.practicum.server.compilation.service;

import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.dto.CompilationDto;
import ru.practicum.server.compilation.model.dto.CompilationDtoPatch;

import java.util.Collection;

public interface CompilationService {
    Compilation create(CompilationDto compDto);

    void delete(long compId);

    Compilation update(long compId, CompilationDtoPatch compDto);

    Compilation get(long compId);

    Collection<Compilation> getFiltered(boolean pinned, int from, int size);
}
