package ru.practicum.service.impl;

import com.querydsl.core.BooleanBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventSort;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.*;
import ru.practicum.dto.event.state.AdminStateAction;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.dto.event.state.UserStateAction;
import ru.practicum.exception.LocalDateTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.UpdateEventException;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.LocationMapper;
import ru.practicum.model.*;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LocationRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.EventService;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private final EventRepository repository;
    private final CategoryRepository categoryRepository;
    private final StatsService statsService;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<EventFullDto> adminGetEvents(GetEventsAdminRequestParams params) {
        log.info("Получение событий админом. params: {}", params);
        BooleanBuilder predicate = getAdminPredicate(params);
        return EventMapper.toDto(repository.findAll(predicate, PageRequest.of(params.getFrom(), params.getSize())));
    }

    @Override
    @Transactional
    public EventFullDto adminUpdateEvent(int id, UpdateEventAdminRequest req) {
        log.info("Обновление события админом. id: {}, request: {}", id, req);
        Event event = findEventByIdOrThrow(id);
        if (req.getStateAction() != null) {
            if (event.getState() == EventState.CANCELED) {
                throw new UpdateEventException("Нельзя изменить отмененное событие");
            }
            if (req.getStateAction() == AdminStateAction.PUBLISH_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new UpdateEventException("Нельзя опубликовать уже опубликованное событие");
                }
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else if (req.getStateAction() == AdminStateAction.REJECT_EVENT) {
                if (event.getState() == EventState.PUBLISHED) {
                    throw new UpdateEventException("Нельзя отклонить уже опубликованное событие");
                }
                event.setState(EventState.CANCELED);
            }
        }
        updateEvent(event, req);
        return EventMapper.toDto(event);
    }

    @Override
    public List<EventShortDto> getEvents(GetEventsRequestParams params) {
        log.info("Получение событий. params: {}", params);
        List<Event> events;
        BooleanBuilder predicate = getPublicPredicate(params);
        if (params.getRangeStart() != null &&
                params.getRangeEnd() != null &&
                params.getRangeStart().isAfter(params.getRangeEnd())) {
            throw new LocalDateTimeException("Дата старта не может быть позже даты окончания");
        }
        if (params.getEventSort() == EventSort.EVENT_DATE) {
            events = repository.findAll(predicate, PageRequest.of(
                            params.getFrom(),
                            params.getSize(),
                            Sort.by(Sort.Direction.DESC, "eventDate"))).stream()
                    .toList();
        } else {
            List<ViewStatsDto> views = statsService.getStats(
                            LocalDateTime.now().minusYears(1),
                            LocalDateTime.now(),
                            null,
                            true).stream()
                    .filter(dto -> StringUtils.countMatches(dto.getUri(), "/") == 2)
                    .collect(Collectors.toList());
            List<Integer> ids = getPopularEventIds(views, params.getFrom(), params.getSize());
            events = repository.findAllByIdIn(ids);
        }
        setViewsToEvents(events);
        return EventMapper.toShortDto(events);
    }

    @Override
    public EventFullDto getEvent(int id) {
        log.info("Получение события. id: {}", id);
        Event event = findEventByIdOrThrow(id);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с id " + id + " не найдено");
        }
        setViewsToEvent(event);
        return EventMapper.toDto(event);
    }

    @Override
    public List<EventShortDto> userGetEvents(int userId, int from, int size) {
        log.info("Получение событий пользователя. userId: {}, from: {}, size: {}", userId, from, size);
        List<Event> events = repository.findAllByInitiateId(userId, PageRequest.of(from, size));
        setViewsToEvents(events);
        return EventMapper.toShortDto(events);
    }

    @Override
    @Transactional
    public EventFullDto userAddEvent(int userId, NewEventDto dto) {
        log.info("Добавление события пользователем. userId: {}, dto: {}", userId, dto);
        User user = findUserByIdOrThrow(userId);
        Category category = findCategoryByIdOrThrow(dto.getCategory());
        Location location = findOrSaveAndGetLocation(dto.getLocation());
        return EventMapper.toDto(repository.save(EventMapper.toEntity(dto, user, category, location)));
    }

    @Override
    public EventFullDto userGetEvent(int userId, int eventId) {
        log.info("Получение события пользователем. userId: {}, eventId: {}", userId, eventId);
        Event event = repository.findByIdAndInitiateId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Событие с id " + eventId + " не найдено"));
        setViewsToEvent(event);
        return EventMapper.toDto(event);
    }

    @Override
    @Transactional
    public EventFullDto userUpdateEvent(int userId, int eventId, UpdateEventUserRequest req) {
        log.info("Обновление события пользователем. userId: {}, eventId: {}, request: {}", userId, eventId, req);
        findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        if (event.getState() == EventState.PUBLISHED) {
            throw new UpdateEventException("Опубликованные события не могут быть изменены");
        }
        if (event.getInitiate().getId() != userId) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        if (req.getStateAction() == UserStateAction.SEND_TO_REVIEW) {
            event.setState(EventState.PENDING);
        } else if (req.getStateAction() == UserStateAction.CANCEL_REVIEW) {
            event.setState(EventState.CANCELED);
        }
        updateEvent(event, req);
        return EventMapper.toDto(event);
    }

    private void setViewsToEvents(List<Event> events) {
        List<Integer> ids = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        LocalDateTime min = events.stream()
                .map(Event::getCreatedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
        if (min == null) {
            return;
        }
        LocalDateTime max = LocalDateTime.now();
        Map<Integer, Long> idsViews = statsService.getStats(min, max, ids, true).stream()
                .collect(Collectors.toMap(view -> extractIdFromUrl(view.getUri()), ViewStatsDto::getHits));
        for (Event event : events) {
            event.setViews(idsViews.containsKey(event.getId()) ? idsViews.get(event.getId()) : 0);
        }
    }

    private void setViewsToEvent(Event event) {
        if (event.getState() != EventState.PUBLISHED) {
            return;
        }
        List<ViewStatsDto> dtos = statsService.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of(event.getId()), true);
        if (!dtos.isEmpty()) {
            event.setViews(dtos.get(0).getHits());
        }
    }

    private List<Integer> getPopularEventIds(List<ViewStatsDto> views, int from, int size) {
        List<Integer> eventsIds = new ArrayList<>();
        int min = Math.min(from * size, views.size());
        int max = Math.min(min + size, views.size());
        for (ViewStatsDto view : views.subList(min, max)) {
            eventsIds.add(extractIdFromUrl(view.getUri()));
        }
        return eventsIds;
    }

    private int extractIdFromUrl(String url) {
        Pattern pattern = Pattern.compile("^/events/(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return 0;
    }

    private void updateEvent(Event event, BaseUpdateRequest<?> req) {
        if (req.getCategory() != null) {
            Category category = findCategoryByIdOrThrow(req.getCategory());
            event.setCategory(category);
        }
        if (req.getLocation() != null) {
            Location location = findOrSaveAndGetLocation(req.getLocation());
            event.setLocation(location);
        }
        event.setAnnotation(req.getAnnotation() != null ? req.getAnnotation() : event.getAnnotation());
        event.setDescription(req.getDescription() != null ? req.getDescription() : event.getDescription());
        event.setEventDate(req.getEventDate() != null ? req.getEventDate() : event.getEventDate());
        event.setPaid(req.getPaid() != null ? req.getPaid() : event.isPaid());
        event.setParticipantLimit(req.getParticipantLimit() != null ? req.getParticipantLimit() : event.getParticipantLimit());
        event.setRequestModeration(req.getRequestModeration() != null ? req.getRequestModeration() : event.isRequestModeration());
        event.setTitle(req.getTitle() != null ? req.getTitle() : event.getTitle());
    }

    private Location findOrSaveAndGetLocation(LocationDto location) {
        Location findedLocation = locationRepository.findByLatAndLon(location.getLat(), location.getLon());
        if (findedLocation == null) {
            findedLocation = locationRepository.save(LocationMapper.toEntity(location));
        }
        return findedLocation;
    }

    private BooleanBuilder getAdminPredicate(GetEventsAdminRequestParams params) {
        QEvent event = QEvent.event;
        BooleanBuilder predicate = getPredicate(params);
        if (params.getUsers() != null) {
            predicate.and(event.initiate.id.in(params.getUsers()));
        }
        if (params.getStates() != null) {
            predicate.and(event.state.in(params.getStates()));
        }
        return predicate;
    }

    private BooleanBuilder getPublicPredicate(GetEventsRequestParams params) {
        QEvent event = QEvent.event;
        BooleanBuilder predicate = getPredicate(params);
        predicate.and(event.state.eq(EventState.PUBLISHED));
        if (params.getText() != null) {
            predicate.and(event.annotation.containsIgnoreCase(params.getText())
                    .or(event.description.containsIgnoreCase(params.getText())));
        }
        if (params.getPaid() != null && params.getPaid()) {
            predicate.and(event.paid.eq(true));
        }
        if (params.getOnlyAvailable() != null) {
            predicate.and(event.participantLimit.eq(0).or(event.confirmedRequests.lt(event.participantLimit)));
        }
        return predicate;
    }

    private BooleanBuilder getPredicate(BaseEventRequestParams params) {
        QEvent event = QEvent.event;
        BooleanBuilder predicate = new BooleanBuilder();

        if (params.getRangeStart() != null) {
            predicate.and(event.eventDate.after(params.getRangeStart()));
        } else {
            predicate.and(event.eventDate.after(LocalDateTime.now()));
        }
        if (params.getRangeEnd() != null) {
            predicate.and(event.eventDate.before(params.getRangeEnd()));
        }
        if (params.getCategories() != null) {
            predicate.and(event.category.id.in(params.getCategories()));
        }
        return predicate;
    }

    private Event findEventByIdOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }

    private User findUserByIdOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    private Category findCategoryByIdOrThrow(int id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Категория с id " + id + " не найдена"));
    }
}
