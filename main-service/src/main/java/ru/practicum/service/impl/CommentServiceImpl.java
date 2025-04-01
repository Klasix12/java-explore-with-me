package ru.practicum.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentRequestDto;
import ru.practicum.dto.event.state.EventState;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.CommentService;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final EventRepository repository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public CommentDto userAddComment(int userId, int eventId, CommentRequestDto req) {
        log.info("Добавление комментария. userId: {}, eventId: {}, request: {}", userId, eventId, req);
        User user = findUserByIdOrThrow(userId);
        Event event = findEventByIdOrThrow(eventId);
        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Событие с id " + eventId + " не найдено");
        }
        Comment comment = commentRepository.save(CommentMapper.toEntity(req, user, event));
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public CommentDto updateComment(int userId, int eventId, int commentId, CommentRequestDto req) {
        log.info("Обновление комментария. userId: {}, eventId: {}, request: {}", userId, eventId, req);
        findUserByIdOrThrow(userId);
        findEventByIdOrThrow(eventId);
        Comment comment = findCommentByIdOrThrow(commentId);
        if (comment.getEvent().getId() != eventId || comment.getCreator().getId() != userId) {
            throw new NotFoundException("Комментарий с id " + commentId + " не найден");
        }
        comment.setText(req.getText());
        return CommentMapper.toDto(comment);
    }

    @Override
    @Transactional
    public void deleteComment(int userId, int eventId, int commentId) {
        log.info("Удаление комментария. userId: {}, eventId: {}, commentId: {}", userId, eventId, commentId);
        findUserByIdOrThrow(userId);
        findEventByIdOrThrow(eventId);
        Comment comment = findCommentByIdOrThrow(commentId);
        if (comment.getEvent().getId() != eventId || comment.getCreator().getId() != userId) {
            throw new NotFoundException("Комментарий с id " + commentId + " не найден");
        }
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public void adminDeleteComment(int eventId, int commentId) {
        log.info("Администратор удаляет комментарий. eventId: {}, commentId: {}", eventId, commentId);
        Comment comment = findCommentByIdOrThrow(commentId);
        commentRepository.delete(comment);
    }

    @Override
    public List<CommentDto> getComments(int eventId, int from, int size) {
        log.info("Получение комментариев. eventId: {}, from: {}, size: {}", eventId, from, size);
        findEventByIdOrThrow(eventId);
        return CommentMapper.toDto(commentRepository.findAllByEventId(eventId));
    }

    private Event findEventByIdOrThrow(int id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Событие с id " + id + " не найдено"));
    }

    private User findUserByIdOrThrow(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    private Comment findCommentByIdOrThrow(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Комментарий с id " + id + " не найден"));
    }
}
