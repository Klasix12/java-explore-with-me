package ru.practicum.controller.comment;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentRequestDto;
import ru.practicum.service.CommentService;
import ru.practicum.validator.Update;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/comment")
public class UserCommentController {
    private final CommentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Integer eventId,
                                 @PathVariable Integer userId,
                                 @RequestBody @Valid CommentRequestDto req) {
        log.trace("Добавление комментария");
        return service.userAddComment(userId, eventId, req);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable Integer eventId,
                                    @PathVariable Integer userId,
                                    @PathVariable Integer commentId,
                                    @RequestBody @Validated(Update.class) CommentRequestDto req) {
        log.trace("Обновление комментария");
        return service.updateComment(eventId, userId, commentId, req);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer eventId,
                              @PathVariable Integer userId,
                              @PathVariable Integer commentId) {
        log.trace("Удаление комментария");
        service.deleteComment(eventId, userId, commentId);
    }
}
