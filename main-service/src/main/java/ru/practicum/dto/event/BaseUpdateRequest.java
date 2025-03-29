package ru.practicum.dto.event;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.LocationDto;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class BaseUpdateRequest<T> {
    private String annotation;

    @Size(min = 20, max = 7000)
    private String description;

    private Integer category;

    private LocalDateTime eventDate;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero
    private Integer participantLimit;

    private Boolean requestModeration;

    private T stateAction;

    private String title;
}
