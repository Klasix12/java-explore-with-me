package ru.practicum.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.dto.ApiErrorDto;
import ru.practicum.exception.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleBadRequest(MethodArgumentTypeMismatchException e) {
        return getDefaultError(e, "Ошибка в запросе", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return getDefaultError(e, "Ошибка валидации данных", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleLocalDateTimeException(LocalDateTimeException e) {
        return getDefaultError(e, "Ошибка при валидации даты", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleNotFoundException(NotFoundException e) {
        return getDefaultError(e, "Не найдено", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleCategoryNotEmptyException(CategoryNotEmptyException e) {
        return getDefaultError(e, "Существуют события, связанные с категорией", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return getDefaultError(e, "Нарушение целостности данных", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleUpdateEventException(UpdateEventException e) {
        return getDefaultError(e, "Ошибка при обновлении события", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorDto handleUpdateRequestException(UpdateRequestException e) {
        return getDefaultError(e, "Ошибка при создании запроса на участие", HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleThrowable(Throwable e) {
        return getDefaultError(e, "Внутренняя ошибка", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ApiErrorDto getDefaultError(Throwable e, String reason, HttpStatus status) {
        return ApiErrorDto.builder()
                .message(e.getMessage())
                .reason(reason)
                .timestamp(LocalDateTime.now())
                .status(status.getReasonPhrase().toUpperCase())
                .build();
    }
}
