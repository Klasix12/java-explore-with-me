package ru.practicum.service;

import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentRequestDto;

import java.util.List;

public interface CommentService {
    CommentDto userAddComment(int userId, int eventId, CommentRequestDto req);

    CommentDto updateComment(int userId, int eventId, int commentId, CommentRequestDto req);

    void deleteComment(int userId, int eventId, int commentId);

    void adminDeleteComment(int eventId, int commentId);

    List<CommentDto> getComments(int eventId, int from, int size);
}
