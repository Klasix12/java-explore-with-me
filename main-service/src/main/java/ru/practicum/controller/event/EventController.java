package ru.practicum.controller.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EventSort;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.GetEventsRequestParams;
import ru.practicum.service.EventService;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService service;
    private final StatsService statsService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Integer> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime rangeStart,
                                         @RequestParam(required = false)
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         LocalDateTime rangeEnd,
                                         @RequestParam(required = false) Boolean onlyAvailable,
                                         @RequestParam(defaultValue = "EVENT_DATE") EventSort eventSort,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         HttpServletRequest req) {
        log.trace("Получение событий");
        var params = new GetEventsRequestParams(text, paid, onlyAvailable, eventSort, categories, rangeStart, rangeEnd, from, size);
        statsService.hitEndpoint(req);
        return service.getEvents(params);
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Integer id, HttpServletRequest req) {
        log.trace("Получение события");
        statsService.hitEndpoint(req);
        return service.getEvent(id);
    }
}
