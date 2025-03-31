package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.dto.event.state.RequestStatus;

import java.util.List;

@Data
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}
