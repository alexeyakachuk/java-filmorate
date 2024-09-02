package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    @Autowired
    public FilmService( FilmStorage filmStorage,
                        UserStorage userStorage,
                       GenreStorage genreStorage,

                       LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
    }


    public List<Film> findAllFilm() {
        List<Film> allFilm = filmStorage.findAllFilm();

        List<Film> newFindAllFilms = new ArrayList<>();
        for (Film film : allFilm) {
            Film populatedFilm = populateFilmDetails(film);
            newFindAllFilms.add(populatedFilm);
        }
        return newFindAllFilms;
    }

    public Film findFilm(long id) {
        Film film = filmStorage.findFilm(id);
        //находим likeStorage.findLikes
        List<Long> likes = likeStorage.findByIdUserLikes(id);
        film.setLikes(new HashSet<>(likes));
        //также mpa
        //жанры
        List<Genre> genres = genreStorage.findByFilmId(id);
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    public Film createFilm(Film newFilm) {
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        return filmStorage.updateFilm(newFilm);
    }

    public void addLike(long filmId, long userId) {
        if (userStorage.findUser(userId) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }

        if (filmStorage.findFilm(filmId) == null) {
            throw new NotFoundException("Такого фильма нет");
        }

        filmStorage.addLike(filmId, userId);

        log.info("Пользователь с id {} лайкнул фильм {}", userId, filmId);
    }

    public List<Film> popular(int size) {
        List<Film> popularFilms = filmStorage.getPopularFilms(size);

        List<Film> newPopularFilms = new ArrayList<>();
        for (Film film : popularFilms) {
            Film populatedFilm = populateFilmDetails(film);
            newPopularFilms.add(populatedFilm);
        }
        return newPopularFilms;
    }

    private Film populateFilmDetails(Film film) {
        Long id = film.getId();
        List<Genre> genres = genreStorage.findByFilmId(id);
        List<Long> likes = likeStorage.findByIdUserLikes(id);
        film.setGenres(new HashSet<>(genres));
        film.setLikes(new HashSet<>(likes));
        return film;
    }

    public void deleteLike(long filmId, long userId) {
        if (userStorage.findUser(userId) == null) {
            throw new NotFoundException("Такого пользователя нет");
        }
        Film film = filmStorage.findFilm(filmId);

        if (film == null) {
            throw new NotFoundException("Такого фильма нет");
        }

        filmStorage.deleteLike(filmId, userId);

        log.info("Пользователь с id {} удали лайк из фильма {}", userId, film.getName());
    }
}



