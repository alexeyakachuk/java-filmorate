package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        List<Film> films = new ArrayList<>(this.films.values());
        return films;
    }

    @Override
    public Film createFilm(Film newFilm) {
        validate(newFilm);

        long nextId = getNextId();
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        log.info("Создан фильм с id {}", nextId);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        if (films.get(newFilm.getId()) == null) {
            log.warn("Невозможно обновить фильм");
            throw new ValidationException("Невозможно обновить фильм");
        }

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

    private void validate(Film newFilm) {
        try {
            LocalDate releaseDate = newFilm.getReleaseDate();
            if (!isReleaseDate(releaseDate)) {
                throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
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
