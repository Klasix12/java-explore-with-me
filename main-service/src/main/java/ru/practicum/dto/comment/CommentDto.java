package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.util.DateFormat;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentDto {
    private int id;
    private UserShortDto creator;
    private String text;
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    private LocalDateTime created;
}
