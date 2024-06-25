package ru.yandex.practicum.filmorate.myAnnotation.annotation;

import jakarta.validation.Constraint;
import ru.yandex.practicum.filmorate.myAnnotation.validator.DateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = DateValidator.class)
public @interface AfterOrEqualData {
    String message() default "Дата релиза фильма не может быть раньше 28 декабря 1895 года";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    String value() default "1895-12-28";
}
