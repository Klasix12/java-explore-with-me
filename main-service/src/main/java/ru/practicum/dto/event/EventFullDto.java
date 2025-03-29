package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.dto.user.UserShortDto;

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private UserShortDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    private LocalDateTime eventDate;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}

