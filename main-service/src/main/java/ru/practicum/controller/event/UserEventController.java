package ru.practicum.controller.event;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.service.EventService;
import ru.practicum.service.ParticipationService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/events")
public class UserEventController {

    private final EventService service;
    private final ParticipationService participationService;

    @GetMapping
    public List<EventShortDto> getEvents(@PathVariable Integer userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.trace("Получение событий");
        return service.userGetEvents(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Integer userId,
                                 @Valid @RequestBody NewEventDto dto) {
        log.trace("Добавление события");
        return service.userAddEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Integer userId,
                                 @PathVariable Integer eventId) {
        log.trace("Получение события");
        return service.userGetEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Integer userId,
                                    @PathVariable Integer eventId,
                                    @Valid @RequestBody UpdateEventUserRequest req) {
        return service.userUpdateEvent(userId, eventId, req);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Integer userId,
                                                     @PathVariable Integer eventId) {
        log.trace("Получение запросов на участие");
        return participationService.getEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Integer eventId,
                                                         @PathVariable Integer userId,
                                                         @RequestBody EventRequestStatusUpdateRequest req) {
        log.trace("Обновление запроса на участие");
        return participationService.updateRequests(userId, eventId, req);
    }
}
