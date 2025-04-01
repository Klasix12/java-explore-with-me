package ru.practicum.controller.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.CommentService;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/admin/events/{eventId}/comment")
public class AdminCommentController {
    private final CommentService service;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Integer eventId,
                              @PathVariable Integer commentId) {
        log.trace("Удаление комментария");
        service.adminDeleteComment(eventId, commentId);
    }
}
