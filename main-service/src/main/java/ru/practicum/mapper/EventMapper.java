package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Streamable;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Location;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static List<EventFullDto> toDto(Streamable<Event> events) {
        return events.stream()
                .map(EventMapper::toDto)
                .collect(Collectors.toList());
    }

    public static List<EventShortDto> toShortDto(List<Event> events) {
        return events == null ? Collections.emptyList() : events.stream()
                .map(EventMapper::toShortDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto toShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .category(CategoryMapper.toDto(event.getCategory()))
                .description(event.getDescription())
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.toShortDto(event.getInitiate()))
                .paid(event.isPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto toDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toDto(event.getCategory()))
                .description(event.getDescription())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .initiator(UserMapper.toShortDto(event.getInitiate()))
                .location(LocationMapper.toDto(event.getLocation()))
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .eventDate(event.getEventDate())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEntity(NewEventDto dto, User user, Category category, Location location) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .title(dto.getTitle())
                .eventDate(dto.getEventDate())
                .createdOn(LocalDateTime.now())
                .category(category)
                .location(location)
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .state(EventState.PENDING)
                .initiate(user)
                .build();
    }
}
