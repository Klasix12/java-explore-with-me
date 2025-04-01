package ru.practicum.dto.event;

import lombok.Getter;
import lombok.ToString;
import ru.practicum.dto.EventSort;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
public class GetEventsRequestParams extends BaseEventRequestParams {

    private String text;

    private Boolean paid;

    private Boolean onlyAvailable;

    private EventSort eventSort;

    public GetEventsRequestParams(String text,
                                  Boolean paid,
                                  Boolean onlyAvailable,
                                  EventSort eventSort,
                                  List<Integer> categories,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Integer from,
                                  Integer size) {
        super(categories, rangeStart, rangeEnd, from, size);
        this.text = text;
        this.paid = paid;
        this.onlyAvailable = onlyAvailable;
        this.eventSort = eventSort;
    }
}
