package ru.practicum.dto.event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.dto.event.state.RequestStatus;

import java.util.List;

@Getter
@Setter
@ToString
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
