package ru.practicum.controller.event;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.GetEventsAdminRequestParams;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.service.EventService;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService service;

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) List<Integer> users,
                                        @RequestParam(required = false) List<EventState> states,
                                        @RequestParam(required = false) List<Integer> categories,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
                                        LocalDateTime rangeStart,
                                        @RequestParam(required = false)
                                        @DateTimeFormat(pattern = DateFormat.DATE_TIME_FORMAT)
                                        LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        log.trace("Получение событий");
        var params = new GetEventsAdminRequestParams(users, states, categories, rangeStart, rangeEnd, from, size);
        return service.adminGetEvents(params);
    }

    @PatchMapping("/{id}")
    public EventFullDto updateEvent(@PathVariable Integer id,
                                    @Valid @RequestBody UpdateEventAdminRequest req) {
        log.trace("Обновление события");
        return service.adminUpdateEvent(id, req);
    }
}
