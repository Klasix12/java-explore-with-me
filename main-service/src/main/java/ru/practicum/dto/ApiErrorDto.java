package ru.practicum.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto {
    private List<String> errors;
    private String message;
    private String reason;
    private String status;
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime timestamp;
}
