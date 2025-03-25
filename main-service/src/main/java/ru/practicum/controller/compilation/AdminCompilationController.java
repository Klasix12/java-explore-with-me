package ru.practicum.controller.compilation;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class AdminCompilationController {

    private final CompilationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody NewCompilationDto dto) {
        log.trace("Добавление подборки");
        return service.addCompilation(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Integer id) {
        log.trace("Удаление подборки");
        service.deleteCompilation(id);
    }

    @PatchMapping("/{id}")
    public CompilationDto updateCompilation(@PathVariable Integer id,
                                            @Valid @RequestBody UpdateCompilationRequest req) {
        log.trace("Обновление подборки");
        return service.updateCompilation(id, req);
    }
}
