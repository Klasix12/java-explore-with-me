package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(*)) " +
            "FROM EndpointHit AS e " +
            "WHERE timestamp >= :start AND timestamp <= :end " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAllByTimestamp(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE timestamp >= :start AND timestamp <= :end " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAllByTimestampUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE timestamp >= :start AND timestamp <= :end " +
            "AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAllByTimestampAndUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new ru.practicum.dto.ViewStatsDto(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit AS e " +
            "WHERE timestamp >= :start AND timestamp <= :end " +
            "AND e.uri IN (:uris) " +
            "GROUP BY e.app, e.uri")
    List<ViewStatsDto> findAllByTimestampAndUrisUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}
