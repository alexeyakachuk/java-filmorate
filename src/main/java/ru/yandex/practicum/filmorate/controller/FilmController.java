package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
@Getter
@RequiredArgsConstructor
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>(this.films.values());
        films.sort(Comparator.comparing(Film::getName).thenComparing(Film::getReleaseDate));
        return films;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) {
        validate(newFilm);

        long nextId = getNextId();
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Создан фильм с id {}", nextId);
        return newFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {

        checkId(newFilm);
        validate(newFilm);
        long nextId = getNextId();
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм с id {} обновлен", nextId);
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

            LocalDate releaseDate = newFilm.getReleaseDate();
            if (!isReleaseDate(releaseDate)) {
                throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            }
//            if (newFilm.toString().isEmpty()) {
//                throw new ValidationException("cds");
//            }


        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
