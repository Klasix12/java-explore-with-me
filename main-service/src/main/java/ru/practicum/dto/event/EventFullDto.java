package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.practicum.dto.LocationDto;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto extends EventShortDto {

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime createdOn;

    private LocationDto location;

    private Integer participantLimit;

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;
}
