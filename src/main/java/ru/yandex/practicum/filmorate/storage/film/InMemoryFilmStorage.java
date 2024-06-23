package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@Component
@Getter
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAllFilm() {
        List<Film> films = new ArrayList<>(this.films.values());
        return films;
    }

    @Override
    public Film findFilm(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильма с таким id " + id + " нет");
        }
        return films.get(id);
    }

    @Override
    public Film createFilm(Film newFilm) {
        validate(newFilm);

        newFilm.setLike(new HashSet<>());

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
            throw new NotFoundException("Такого фильма нет");
        }

        Long id = newFilm.getId();
        Film oldFilm = films.get(id);

        checkId(newFilm);
        validate(newFilm);
        updateFields(oldFilm, newFilm);
        films.put(id, oldFilm);
        log.info("Фильм с id {} обновлен", id);
        return oldFilm;
    }

    private void checkId(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ValidationException("Id фильма должен быть указан");
        }
    }

    private void updateFields(Film oldFilm, Film newFilm) {
        oldFilm.setName(newFilm.getName());

        if (newFilm.getDescription() != null) {
            oldFilm.setDescription(newFilm.getDescription());
        }
        if (newFilm.getDuration() != 0) {
            oldFilm.setDuration(newFilm.getDuration());
        }
        if (newFilm.getLike() != null) {
            oldFilm.setLike(newFilm.getLike());
        }

        oldFilm.setReleaseDate(newFilm.getReleaseDate());
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
