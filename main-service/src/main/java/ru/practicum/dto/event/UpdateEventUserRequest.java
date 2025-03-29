package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.dto.event.state.UserStateAction;
import ru.practicum.util.DateFormat;
import ru.practicum.validator.FutureFrom;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class UpdateEventUserRequest extends BaseUpdateRequest<UserStateAction> {

    @Override
    @JsonFormat(pattern = DateFormat.DATE_TIME_FORMAT)
    @FutureFrom(message = "дата и время на которые намечено событие не может быть раньше, чем за 2 часа от текущего момента", duration = 2)
    public LocalDateTime getEventDate() {
        return super.getEventDate();
    }
}
