package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.event.state.AdminStateAction;
import ru.practicum.validator.FutureFrom;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class UpdateEventAdminRequest extends BaseUpdateRequest<AdminStateAction> {

    @Override
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @FutureFrom(message = "дата и время на которые намечено событие не может быть раньше, чем за час от текущего момента", duration = 1)
    public LocalDateTime getEventDate() {
        return super.getEventDate();
    }
}
