package ru.practicum.dto.event;

import lombok.Getter;
import lombok.ToString;
import ru.practicum.dto.event.state.EventState;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class GetEventsAdminRequestParams extends BaseEventRequestParams {

    private final List<Integer> users;

    private final List<EventState> states;

    public GetEventsAdminRequestParams(List<Integer> users,
                                       List<EventState> states,
                                       List<Integer> categories,
                                       LocalDateTime rangeStart,
                                       LocalDateTime rangeEnd,
                                       Integer from,
                                       Integer size) {
        super(categories, rangeStart, rangeEnd, from, size);
        this.users = users;
        this.states = states;
    }
}
