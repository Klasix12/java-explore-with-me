package ru.practicum.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Service
public class StatsClientService {
    private final RestTemplate restTemplate;
    private static final DateTimeFormatter format = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public StatsClientService(@Value("${server.url}") String serverUrl, RestTemplateBuilder builder) {
        restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build();
    }

    public void hit(EndpointHitDto dto) {
        restTemplate.postForObject("/hit", dto, Void.class);
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/stats");
        if (start != null) {
            builder.queryParam("start", start.format(format));
        }
        if (end != null) {
            builder.queryParam("end", end.format(format));
        }
        if (uris != null && !uris.isEmpty()) {
            builder.queryParam("uris", String.join(",", uris));
        }
        if (unique) {
            builder.queryParam("unique", true);
        }
        ViewStatsDto[] response = restTemplate.getForObject(builder.encode().toUriString(), ViewStatsDto[].class);
        return response == null ? Collections.emptyList() : List.of(response);
    }
}
