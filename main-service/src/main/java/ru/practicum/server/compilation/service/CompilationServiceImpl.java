package ru.practicum.server.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.model.dto.CompilationDto;
import ru.practicum.server.compilation.model.dto.CompilationDtoPatch;
import ru.practicum.server.compilation.model.dto.mapper.CompilationMapper;
import ru.practicum.server.compilation.repository.CompilationRepository;
import ru.practicum.server.event.model.Event;
import ru.practicum.server.event.repository.EventRepository;
import ru.practicum.server.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public Compilation create(CompilationDto compDto) {
        Compilation compilation = CompilationMapper.mapToCompilation(compDto);
        if (compDto.getEvents() == null || compDto.getEvents().isEmpty()) {
            compilation.setEvents(new ArrayList<>());
        } else {
            List<Event> events = eventRepository.findAllById(compDto.getEvents());
            compilation.setEvents(events);
        }
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void delete(long compId) {
        compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        compilationRepository.deleteById(compId);
        log.info("DELETE /admin/compilations/{} -> deleted from db", compId);
    }

    @Override
    @Transactional
    public Compilation update(long compId, CompilationDtoPatch compDto) {
        Compilation old = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        Compilation compilation = CompilationMapper.updateCompilation(compDto, old);
        compilation.setId(compId);
        if (compDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllById(compDto.getEvents()));
        }
        return compilationRepository.save(compilation);
    }

    @Override
    @Transactional(readOnly = true)
    public Compilation get(long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        log.info("GET /compilations/{} -> returning from db {}", compId, compilation);
        return compilation;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<Compilation> getFiltered(boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        log.info("GET /compilations -> returning from db");
        return compilations;
    }
}
