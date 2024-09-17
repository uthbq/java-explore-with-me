package ru.practicum.server.compilation.model.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.dto.CompilationDto;
import ru.practicum.server.compilation.model.dto.CompilationDtoPatch;

@UtilityClass
public class CompilationMapper {
    public static Compilation mapToCompilation(CompilationDto dto) {
        return new Compilation(dto.getPinned(), dto.getTitle());
    }

    public static Compilation updateCompilation(CompilationDtoPatch dto, Compilation old) {
        if (dto.getPinned() != null) {
            old.setPinned(dto.getPinned());
        }
        if (dto.getTitle() != null) {
            if (!old.getTitle().isBlank()) {
                old.setTitle(old.getTitle());
            }
        }
        return old;
    }
}
