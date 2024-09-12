package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikeStorage;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage,
                       UserStorage userStorage,
                       GenreStorage genreStorage,
                       LikeStorage likeStorage,
                       MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.likeStorage = likeStorage;
        this.mpaStorage = mpaStorage;
    }


    public List<Film> findAllFilm() {
        List<Film> allFilm = filmStorage.findAllFilm();

        for (Film film : allFilm) {
            populateFilmDetails(film);
        }
        return allFilm;
    }

    public Film findFilm(long id) {
        Film film = filmStorage.findFilm(id);
        //находим likeStorage.findLikes
        List<Long> likes = likeStorage.findByIdUserLikes(id);
        film.setLikes(new HashSet<>(likes));
        //жанры
        List<Genre> genres = genreStorage.findByFilmId(id);
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    public Film createFilm(Film newFilm) {

        int mpaId = newFilm.getMpa().getId();

        try {
            mpaStorage.findById(mpaId);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }

        Film film = filmStorage.createFilm(newFilm);

        try {
            genreStorage.addGenresToFilm(newFilm);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }

        List<Long> likes = likeStorage.findByIdUserLikes(film.getId());
        film.setLikes(new HashSet<>(likes));

        List<Genre> genresByFilmId = genreStorage.findGenresByFilmId(film.getId());
        TreeSet<Genre> genres = new TreeSet<>(Comparator.comparing(Genre::getId));
        genres.addAll(genresByFilmId);
        film.setGenres(genres);

        return film;
    }

    public Film updateFilm(Film updatedFilm) {
        // Проверяем существование MPA
        int mpaId = updatedFilm.getMpa().getId();
        try {
            mpaStorage.findById(mpaId);
        } catch (DataAccessException e) {
            throw new ValidationException(e.getMessage());
        }

        // Обновляем данные фильма в базе данных
        Film updatedFilmFromDb = filmStorage.updateFilm(updatedFilm);

        // Обновляем жанры фильма
        genreStorage.removeGenresFromFilm(updatedFilm.getId());
        genreStorage.addGenresToFilm(updatedFilm);

        // Заполняем обновлённые детали фильма
        updatedFilmFromDb = populateFilmDetails(updatedFilmFromDb);

        return updatedFilmFromDb;
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



