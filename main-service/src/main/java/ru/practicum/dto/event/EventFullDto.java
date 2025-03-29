package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private Integer id;

    private String annotation;

    private CategoryDto category;

    private String description;

    private Integer confirmedRequests;

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime eventDate;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}

