package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CompilationService;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto getCompilation(int id) {
        log.info("Получение подборки. id: {}", id);
        Compilation compilation = getCompilationByIdOrThrow(id);
        return CompilationMapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        log.info("Получение событий. pinned: {}, from: {}, size: {}", pinned, from, size);
        return CompilationMapper.toDto(repository.findByPinned(pinned, PageRequest.of(from, size)));
    }

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto dto) {
        log.info("Добавление новой подборки. request: {}", dto);
        List<Event> events = null;
        if (dto.getEvents() != null && dto.getEvents().size() > 0) {
            events = eventRepository.findAllById(dto.getEvents());
        }
        return CompilationMapper.toDto(repository.save(CompilationMapper.toEntity(dto, events)));
    }

    @Override
    @Transactional
    public void deleteCompilation(int id) {
        log.info("Удаление подборки. id: {}", id);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + id + " не найдена"));
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(int id, UpdateCompilationRequest req) {
        log.info("Обновление подборки. id: {}, request: {}", id, req);
        Compilation compilation = getCompilationByIdOrThrow(id);
        if (req.getPinned() != null && req.getPinned()) {
            compilation.setPinned(true);
        }
        if (req.getEvents() != null) {
            compilation.setEvents(getEventsByIds(req.getEvents()));
        }
        if (req.getTitle() != null) {
            compilation.setTitle(req.getTitle());
        }
        return CompilationMapper.toDto(compilation);
    }

    private Compilation getCompilationByIdOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка с id " + id + " не найдена"));
    }

    private List<Event> getEventsByIds(List<Integer> ids) {
        return eventRepository.findAllByIdIn(ids);
    }
}
