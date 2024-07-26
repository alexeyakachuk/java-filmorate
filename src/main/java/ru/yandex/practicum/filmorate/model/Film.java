package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.myAnnotation.annotation.AfterOrEqualData;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Builder
public class Film {
    private Long id;
    @NotNull
    @NotBlank(message = "Не может быть пустым")
    private String name;
    @Size(max = 200, message = "Не более 200 символов")
    private String description;
    @NotNull
    @AfterOrEqualData
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    private Set<Long> like;
    private Mpa mpa;
    private Set<Genre> genres;
}
