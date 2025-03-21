package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsParamsDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.Collection;

public interface StatsService {
    void hit(EndpointHitDto dto);

    Collection<ViewStatsDto> getStats(StatsParamsDto params);
}
