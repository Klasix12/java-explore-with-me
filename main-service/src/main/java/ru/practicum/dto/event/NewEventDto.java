package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.practicum.dto.LocationDto;
import ru.practicum.validator.FutureFrom;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    private String description;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureFrom(message = "Дата события не должна быть раньше 2 часов от текущего времени", duration = 2)
    private LocalDateTime eventDate;

    private Integer category;

    private LocationDto location;

    private boolean paid;

    private int participantLimit;

    private Boolean requestModeration = true;
}
