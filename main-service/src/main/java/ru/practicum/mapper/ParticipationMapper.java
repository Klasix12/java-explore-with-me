package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.participation.ParticipationRequestDto;
import ru.practicum.model.Participation;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParticipationMapper {

    public static ParticipationRequestDto toDto(Participation participation) {
        return ParticipationRequestDto.builder()
                .id(participation.getId())
                .event(participation.getEvent().getId())
                .created(participation.getCreated())
                .requester(participation.getRequester().getId())
                .status(participation.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toDto(List<Participation> participations) {
        return participations.stream()
                .map(ParticipationMapper::toDto)
                .collect(Collectors.toList());
    }
}
