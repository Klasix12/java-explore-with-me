package ru.practicum.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDto {
    private double lat;
    private double lon;
}
