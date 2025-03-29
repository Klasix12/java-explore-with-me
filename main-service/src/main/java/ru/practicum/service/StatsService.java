package ru.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {
    List<ViewStatsDto> getStats();

    List<ViewStatsDto> getStats(List<Integer> ids);

    void hitEndpoint(HttpServletRequest req);
}
