package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.StatsParamsDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceTest {
    private final StatsRepository repository;
    private final StatsService service;
    private EndpointHitDto dto;

    @BeforeEach
    void setUp() {
        dto = EndpointHitDto.builder()
                .uri("uri")
                .ip("1.2.3.4")
                .app("app")
                .timestamp(LocalDateTime.of(2020,
                        1,
                        1,
                        0,
                        0,
                        0))
                .build();
    }

    @Test
    void hitTest() {
        service.hit(dto);
        EndpointHit savedHit = repository.findAll().get(0);
        assertThatHitHasCorrectFields(savedHit, dto);
    }

    /*
    Всего 4 параметра в запросе, нам нужно протестировать:
    1. запросы с уникальных ip без uris +
    2. запросы с уникальных ip с uris +
    3. запросы с уникальных ip с start +
    4. запросы с уникальных ip с end
    5. запросы с уникальных ip с start и end

    1. запросы с неуникальных ip без uris
    2. запросы с неуникальных ip с uris
    3. запросы с неуникальных ip с start
    4. запросы с неуникальных ip с end
    5. запросы с неуникальных ip с start и end
     */

    @Test
    void getStatsTestUniqueIpWithoutUris() {
        service.hit(dto);
        dto.setIp("1.2.3.5");
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                null,
                null,
                true);
        List<ViewStatsDto> savedHits = service.getStats(params).stream().toList();
        System.out.println(savedHits);
        assertThat(savedHits.size(), equalTo(1));
        assertThat(savedHits.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestUniqueIpWithUris() {
        service.hit(dto);
        dto.setIp("1.2.3.5");
        service.hit(dto);
        dto.setUri("uri2");
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                null,
                List.of("uri"),
                true
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
        assertThat(stats.get(0).getUri(), equalTo("uri"));
    }

    @Test
    void getStatsTestUniqueIpWithStart() {
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(5));
        dto.setUri("uri2");
        dto.setIp("1.2.3.5");
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                dto.getTimestamp().minusMinutes(6),
                null,
                null,
                true
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();
        assertThat(stats.size(), equalTo(2));
        assertThat(stats.get(0).getHits(), equalTo(1L));
    }

    @Test
    void getStatsTestUniqueIpWithEnd() {
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(1));
        dto.setIp("1.2.3.5");
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(2));
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                dto.getTimestamp().minusMinutes(2),
                null,
                true
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();
        System.out.println(stats);

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestUniqueIpWithStartAndEnd() {
        service.hit(dto);
        LocalDateTime start = dto.getTimestamp().minusMinutes(5);
        LocalDateTime end = dto.getTimestamp().plusMinutes(5);
        dto.setIp("1.2.3.5");
        dto.setTimestamp(end);
        service.hit(dto);
        dto.setTimestamp(end.plusMinutes(1));
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                start,
                end,
                null,
                true
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestNonUniqueIpWithoutUris() {
        service.hit(dto);
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                null,
                null,
                false
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestNonUniqueIpWithUris() {
        service.hit(dto);
        service.hit(dto);
        dto.setUri("uri2");
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                null,
                List.of("uri"),
                false
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();
        System.out.println(stats);

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestNonUniqueIpWithStart() {
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(5));
        service.hit(dto);
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                dto.getTimestamp(),
                null,
                null,
                false
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestNonUniqueIpWithEnd() {
        service.hit(dto);
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(1));
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                null,
                dto.getTimestamp().minusMinutes(1),
                null,
                false
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    @Test
    void getStatsTestNonUniqueIpWithStartAndEnd() {
        service.hit(dto);
        service.hit(dto);
        dto.setTimestamp(dto.getTimestamp().plusMinutes(1));
        service.hit(dto);

        StatsParamsDto params = new StatsParamsDto(
                dto.getTimestamp().minusMinutes(1),
                dto.getTimestamp().minusSeconds(30),
                null,
                false
        );
        List<ViewStatsDto> stats = service.getStats(params).stream().toList();

        assertThat(stats.size(), equalTo(1));
        assertThat(stats.get(0).getHits(), equalTo(2L));
    }

    private void assertThatHitHasCorrectFields(EndpointHit hit, EndpointHitDto dto) {
        assertThat(hit.getId(), notNullValue());
        assertThat(hit.getApp(), equalTo(dto.getApp()));
        assertThat(hit.getUri(), equalTo(dto.getUri()));
        assertThat(hit.getIp(), equalTo(dto.getIp()));
    }
}
