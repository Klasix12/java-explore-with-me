package ru.practicum.controller.comment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class CommentController {
    private CommentService service;

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer eventId,
                                        @RequestParam(defaultValue = "0") Integer from,
                                        @RequestParam(defaultValue = "10") Integer size) {
        log.trace("Получение комментариев");
        return service.getComments(eventId, from, size);
    }
}
