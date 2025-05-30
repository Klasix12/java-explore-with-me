package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserShortDto {
    private Integer id;
    private String name;
}
