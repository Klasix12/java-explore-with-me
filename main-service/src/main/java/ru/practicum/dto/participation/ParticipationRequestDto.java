package ru.practicum.dto.participation;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private Integer id;

    private Integer event;

    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime created;

    private Integer requester;

    private ParticipationStatus status;
}

