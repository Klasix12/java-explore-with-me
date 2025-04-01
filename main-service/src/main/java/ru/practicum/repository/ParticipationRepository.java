package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Participation;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Integer> {
    List<Participation> findAllByRequesterId(int userId);

    List<Participation> findAllByEventId(int eventId);

    List<Participation> findAllByIdIn(List<Integer> ids);
}
