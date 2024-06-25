package ru.yandex.practicum.filmorate.myAnnotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.myAnnotation.annotation.AfterOrEqualData;

import java.time.LocalDate;

public class DateValidator implements ConstraintValidator<AfterOrEqualData, LocalDate> {

    LocalDate dateOfRelease;


    @Override
    public void initialize(AfterOrEqualData constraintAnnotation) {
        dateOfRelease = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value == null || !value.isBefore(dateOfRelease);
    }
}
