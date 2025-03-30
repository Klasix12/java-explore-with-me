package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsParamsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.mapper.EndpointHitMapper;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;
    private final LocalDateTime minTimestamp = LocalDateTime.of(1900, 1, 1, 0, 0, 0);
    private final LocalDateTime maxTimestamp = LocalDateTime.of(2999, 1, 1, 0, 0, 0);

    @Override
    @Transactional
    public void hit(EndpointHitDto dto) {
        log.info("Сохранение перехода: {}", dto);
        repository.save(EndpointHitMapper.toEntity(dto));
    }

    @Override
    public Collection<ViewStatsDto> getStats(StatsParamsDto params) {
        log.info("Получение статистики с параметрами: {}", params);
        LocalDateTime start = params.getStart() == null ? minTimestamp : params.getStart();
        LocalDateTime end = params.getEnd() == null ? maxTimestamp : params.getEnd();
        if (start.isAfter(end)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Дата старта не может быть позже даты окончания");
        }
        if (params.getUnique()) {
            if (params.hasUris()) {
                return repository.findAllByTimestampAndUrisUnique(start, end, params.getUris());
            } else {
                return repository.findAllByTimestampUnique(start, end);
            }
        } else {
            if (params.hasUris()) {
                return repository.findAllByTimestampAndUris(start, end, params.getUris());
            } else {
                return repository.findAllByTimestamp(start, end);
            }
        }
    }
}
