package ru.practicum.service;

import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.dto.participation.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getEventRequests(int userId);

    ParticipationRequestDto addRequest(int userId, int eventId);

    ParticipationRequestDto cancelRequest(int userId, int requestId);

    List<ParticipationRequestDto> getEventRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequests(int userId, int eventId, EventRequestStatusUpdateRequest req);
}
