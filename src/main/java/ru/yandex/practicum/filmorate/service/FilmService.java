package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }


    public List<Film> findAllFilm() {
        return filmStorage.findAllFilm();
    }

    public Film findFilm(long id) {
        return filmStorage.findFilm(id);
    }


    public Film createFilm(Film newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public void addLike(long filmId, long userId) {
        Film film = findFilm(filmId);
        // спросить если смысл в этой записи
        User user = userStorage.findUser(userId);
        Set<Long> like = film.getLike();
        // можно так like.add(userId)
        like.add(user.getId());
        updateFilm(film);
        log.info("Пользователь {} лайкнул фильм {}", userStorage.findUser(userId), film.getName());
    }

    public List<Film> popular(int size) {
        List<Film> sortFilm = filmStorage.findAllFilm();
        sortFilm.sort((s1, s2) -> s2.getLike().size() - s1.getLike().size());
        return new ArrayList<>(sortFilm.subList(0, Math.min(size, sortFilm.size())));
    }

    public void deleteLike(long filmId, long userId) {
        if (userStorage.findUser(userId) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        Film film = filmStorage.findFilm(filmId);
        film.getLike().remove(userId);
        updateFilm(film);
        log.info("Пользователь {} удали лайк из фильма {}", userStorage.findUser(userId), film.getName());
    }
}
