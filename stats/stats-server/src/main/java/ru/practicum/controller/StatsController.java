package ru.practicum.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsParamsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsService;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@Valid @RequestBody EndpointHitDto endpointHit) {
        log.trace("Новый переход");
        statsService.hit(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public Collection<ViewStatsDto> getStats(@RequestParam @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT) LocalDateTime start,
                                             @RequestParam @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT) LocalDateTime end,
                                             @RequestParam(required = false) List<String> uris,
                                             @RequestParam(defaultValue = "false") Boolean unique) {
        log.trace("Получение статистики");
        return statsService.getStats(new StatsParamsDto(start, end, uris, unique));
    }
}
