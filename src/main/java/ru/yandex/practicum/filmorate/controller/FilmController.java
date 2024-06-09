package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create (@RequestBody Film newFilm) {
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            throw new ValidationException("Название фильма не должно быть пустым");
        }

        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание  фильма не должно быть больше 200 символов");
        }

        if (newFilm.getReleaseDate() != null) {
            LocalDate releaseDate = newFilm.getReleaseDate();
            if (!isReleaseDate(releaseDate)) {
                throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            }
        }

        if (newFilm.getDuration() != null) {
            Duration duration = newFilm.getDuration();
            if (duration.isNegative() || duration.isZero()) {
                throw new ValidationException("Продолжительность фильма должна быть положительным числом");
            }
        }

        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return null;
    }

    private boolean isReleaseDate(LocalDate releaseDate) {
        LocalDate earliestDate = LocalDate.of(1895, Month.DECEMBER, 28);
        return !releaseDate.isBefore(earliestDate);
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
