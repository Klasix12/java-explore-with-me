package ru.practicum.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class EndpointHitMapperTest {
    @Test
    void toDtoTest() {
        EndpointHit hit = EndpointHit.builder()
                .ip("1.2.3.4")
                .uri("uri")
                .app("app")
                .timestamp(LocalDateTime.now())
                .build();

        EndpointHitDto dto = EndpointHitMapper.toDto(hit);
        assetThatEndpointsHasEqualsFields(dto, hit);
    }

    @Test
    void toEntityTest() {
        EndpointHitDto dto = EndpointHitDto.builder()
                .ip("1.2.3.4")
                .uri("uri")
                .app("app")
                .timestamp(LocalDateTime.now())
                .build();

        EndpointHit hit = EndpointHitMapper.toEntity(dto);
        assetThatEndpointsHasEqualsFields(dto, hit);
    }

    private void assetThatEndpointsHasEqualsFields(EndpointHitDto dto, EndpointHit hit) {
        assertThat(dto.getApp()).isEqualTo(hit.getApp());
        assertThat(dto.getUri()).isEqualTo(hit.getUri());
        assertThat(dto.getIp()).isEqualTo(hit.getIp());
        assertThat(dto.getTimestamp()).isEqualTo(hit.getTimestamp());
    }
}
