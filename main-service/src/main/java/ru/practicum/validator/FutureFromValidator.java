package ru.practicum.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class FutureFromValidator implements ConstraintValidator<FutureFrom, LocalDateTime> {

    private int duration;
    private ChronoUnit unit;

    @Override
    public void initialize(FutureFrom constraintAnnotation) {
        this.duration = constraintAnnotation.duration();
        this.unit = constraintAnnotation.unit();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime future = now.plus(duration, unit);
        return future.isBefore(value);
    }
}
