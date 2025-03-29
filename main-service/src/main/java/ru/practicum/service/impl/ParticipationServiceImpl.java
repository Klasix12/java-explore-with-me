package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.EventRequestStatusUpdateRequest;
import ru.practicum.dto.event.EventRequestStatusUpdateResult;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.dto.event.state.RequestStatus;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.dto.participation.ParticipationStatus;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UpdateEventException;
import ru.practicum.exception.UpdateRequestException;
import ru.practicum.mapper.ParticipationMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Participation;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.ParticipationService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public List<ParticipationRequestDto> getEventRequests(int userId) {
        log.info("Получение пользователем запросов. id: {}", userId);
        return ParticipationMapper.toDto(repository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto addRequest(int userId, int eventId) {
        log.info("Добавление запроса. userId: {}, eventId: {}", userId, eventId);
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        if (userId == event.getInitiate().getId()) {
            throw new UpdateRequestException("Нельзя участвовать в собственном событии");
        }
        if (event.getState() != EventState.PUBLISHED) {
            throw new UpdateRequestException("Нельзя участвовать в неопубликованном событии");
        }
        if (event.getParticipantLimit() != 0 &&
                event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new UpdateRequestException("Достигнут лимит запросов на участие");
        }
        Participation participation = Participation.builder()
                .event(event)
                .created(LocalDateTime.now())
                .requester(user)
                .build();
        if (!event.isRequestModeration() || event.getParticipantLimit() == 0) {
            participation.setStatus(ParticipationStatus.CONFIRMED);
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        }
        return ParticipationMapper.toDto(repository.save(participation));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequest(int userId, int requestId) {
        log.info("Пользователь отменяет свой запрос. userId: {}, requestId: {}", userId, requestId);
        findUserByIdOrThrow(userId);
        Participation participation = repository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с id " + requestId + " не найдено"));
        if (participation.getRequester().getId() != userId) {
            throw new NotFoundException("Запрос с id " + requestId + " не найдено");
        }
        if (participation.getStatus() == ParticipationStatus.CONFIRMED) {
            participation.getEvent().setConfirmedRequests(participation.getEvent().getConfirmedRequests() - 1);
        }
        participation.setStatus(ParticipationStatus.CANCELLED);
        return ParticipationMapper.toDto(participation);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequests(int userId, int eventId) {
        log.info("Пользователь получает запросы на участие в событии. userId: {}, eventId: {}", userId, eventId);
        Event event = findEventByIdOrThrow(eventId);
        if (event.getInitiate().getId() != userId) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        return ParticipationMapper.toDto(repository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequests(int userId, int eventId, EventRequestStatusUpdateRequest req) {
        log.info("Пользователь обновляет запросы на событие. userId: {}, eventId: {}, request: {}", userId, eventId, req);
        List<Participation> participations = repository.findAllByIdIn(req.getRequestsIds());
        findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        if (event.getInitiate().getId() != userId) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        if (event.getParticipantLimit() == 0 || !event.isRequestModeration()) {
            throw new UpdateEventException("Для события с id " + eventId + " нет лимита заявок или отключена пре-модерация заявок");
        }

        var result = new EventRequestStatusUpdateResult();
        ParticipationStatus status = req.getStatus() == RequestStatus.CONFIRMED ? ParticipationStatus.CONFIRMED : ParticipationStatus.REJECTED;

        if (status == ParticipationStatus.CONFIRMED) {
            if (event.getParticipantLimit() >= event.getConfirmedRequests()) {
                throw new UpdateRequestException("Для события с id " + eventId + " достигнут лимит количества участников");
            }
            for (int i = 0; i < Math.min(event.getParticipantLimit() - event.getConfirmedRequests(), participations.size()); i++) {
                Participation updatedParticipation = participations.remove(0);
                updatedParticipation.setStatus(status);
                result.getConfirmedRequests().add(ParticipationMapper.toDto(updatedParticipation));
            }
        }
        participations.forEach(p -> p.setStatus(ParticipationStatus.REJECTED));
        result.setRejectedRequests(ParticipationMapper.toDto(participations));
        return result;
    }

    private Event findEventByIdOrThrow(int id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }

    private User findUserByIdOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }
}
