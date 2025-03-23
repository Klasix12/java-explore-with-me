package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsParamsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;
    private static final String format = "yyyy-MM-dd HH:mm:ss";

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@RequestBody EndpointHitDto endpointHit) {
        log.trace("Новый переход");
        statsService.hit(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ViewStatsDto> getStats(@RequestParam(required = false) @DateTimeFormat(pattern = format) LocalDateTime start,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = format) LocalDateTime end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) {
        log.trace("Получение статистики");
        return statsService.getStats(new StatsParamsDto(start, end, uris, unique));
    }
}
