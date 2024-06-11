package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@Getter
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>(this.films.values());
        films.sort(Comparator.comparing(Film::getName).thenComparing(Film::getReleaseDate));
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film newFilm) {
        validate(newFilm);

        long nextId = getNextId();
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Создан фильм с id {}", nextId);
        return newFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        checkId(newFilm);
        validate(newFilm);
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    private void checkId(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id фильма должен быть указан");
        }
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

    private void validate(Film newFilm) {
        try {
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
// добавил код
                newFilm.setDuration(Duration.ofSeconds(duration.getSeconds()));
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
