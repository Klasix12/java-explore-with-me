package ru.practicum.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Constraint(validatedBy = FutureFromValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
public @interface FutureFrom {
    String message() default "Ошибка времени";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int duration() default 0;

    ChronoUnit unit() default ChronoUnit.HOURS;
}
