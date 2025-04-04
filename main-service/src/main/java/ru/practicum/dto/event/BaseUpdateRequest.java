package ru.practicum.dto.event;

import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.practicum.dto.LocationDto;

import java.time.LocalDateTime;

@Data
public abstract class BaseUpdateRequest<T> {
    @Size(min = 20, max = 2000)
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

    @Size(min = 3, max = 120)
    private String title;
}
