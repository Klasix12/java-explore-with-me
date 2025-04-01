package ru.practicum.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<Integer> ids, Boolean unique);

    void hitEndpoint(HttpServletRequest req);
}
