package ru.practicum.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.service.StatsClientService;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsClientService service;

    @Override
    public List<ViewStatsDto> getStats() {
        log.trace("Получение статистики");
        return service.getStats();
    }

    @Override
    public void hitEndpoint(HttpServletRequest req) {
        log.trace("Переход по эндпоинту");
        service.hit(new EndpointHitDto("ewm", req.getRequestURI(), req.getRemoteAddr(), LocalDateTime.now()));
    }
}
