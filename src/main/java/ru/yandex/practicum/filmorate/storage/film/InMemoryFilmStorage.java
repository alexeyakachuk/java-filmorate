package ru.yandex.practicum.filmorate.storage.film;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

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
        newFilm.setLikes(new HashSet<>());

        long nextId = getNextId();
        newFilm.setId(nextId);
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
        if (newFilm.getLikes() != null) {
            oldFilm.setLikes(newFilm.getLikes());
        }

        oldFilm.setReleaseDate(newFilm.getReleaseDate());
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
