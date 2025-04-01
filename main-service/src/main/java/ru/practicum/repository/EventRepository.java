package ru.practicum.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByIdIn(List<Integer> ids);

    List<Event> findAllByInitiateId(Integer id, Pageable pageable);

    Optional<Event> findByIdAndInitiateId(Integer id, Integer initiateId);
}
