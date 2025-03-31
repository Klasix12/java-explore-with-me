package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EndpointHitDto {
    @NotBlank
    private String app;

    @NotBlank
    private String uri;

    @NotBlank
    private String ip;

    @NotBlank
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
