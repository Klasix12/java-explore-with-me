package ru.practicum.controller.compilation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationController {

    private final CompilationService service;

    @GetMapping("/{id}")
    public CompilationDto getCompilation(@PathVariable Integer id) {
        log.trace("Получение подборки");
        return service.getCompilation(id);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        log.trace("Получение подборок");
        return service.getCompilations(pinned, from, size);
    }
}
