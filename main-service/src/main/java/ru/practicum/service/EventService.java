package ru.practicum.service;

import ru.practicum.dto.event.*;

import java.util.List;

public interface EventService {
    List<EventFullDto> adminGetEvents(GetEventsAdminRequestParams params);

    EventFullDto adminUpdateEvent(int id, UpdateEventAdminRequest req);

    List<EventShortDto> getEvents(GetEventsRequestParams params);

    EventFullDto getEvent(int id);

    List<EventShortDto> userGetEvents(int userId, int from, int size);

    EventFullDto userAddEvent(int userId, NewEventDto dto);

    EventFullDto userGetEvent(int userId, int eventId);

    EventFullDto userUpdateEvent(int userId, int eventId, UpdateEventUserRequest req);
}
