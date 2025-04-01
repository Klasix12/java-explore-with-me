package ru.practicum.service;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto getCompilation(int id);

    List<CompilationDto> getCompilations(boolean pinned, int from, int size);

    CompilationDto addCompilation(NewCompilationDto dto);

    void deleteCompilation(int id);

    CompilationDto updateCompilation(int id, UpdateCompilationRequest req);
}
