package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.filmAndGenre.FilmAndGenre;
import ru.yandex.practicum.filmorate.storage.filmAndGenre.FilmAndGenreStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
@Slf4j
@Getter
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;
    private final FilmAndGenreStorage filmAndGenreStorage;

    @Autowired
    public FilmService(@Qualifier("H2FilmStorage")FilmStorage filmStorage,
                       @Qualifier("H2UserStorage") UserStorage userStorage,
                       GenreStorage genreStorage,
                       @Qualifier("H2FilmAndGenreStorage")FilmAndGenreStorage filmAndGenreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
        this.filmAndGenreStorage = filmAndGenreStorage;
    }


    public List<Film> findAllFilm() {
        List<Film> allFilm = filmStorage.findAllFilm();
        filmStorage.fetchAndSetFilmLikes(allFilm);
        return assignGenresToFilms(allFilm);
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
        List<Film> films = filmStorage.getPopularFilms(size);
        filmStorage.fetchAndSetFilmLikes(films);
        return assignGenresToFilms(films);
    }

    private List<Film> assignGenresToFilms(List<Film> films) {
        List<Genre> allGenre = genreStorage.findAll();
        List<FilmAndGenre> allFilmsAndGenre = filmAndGenreStorage.findAllFilmsAndGenre();

        Map<Long, List<Genre>> filmGenres = new HashMap<>();
        for (FilmAndGenre filmAndGenre : allFilmsAndGenre) {
            long filmId = filmAndGenre.getFilmId();
            int genreId = filmAndGenre.getGenreId();
            Genre genre = allGenre.stream()
                    .filter(g -> g.getId() == genreId)
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Жанр не найден"));
            filmGenres.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
        }

        films.forEach(film -> {
            if (film.getGenres() == null) {
                film.setGenres(new HashSet<>());
            }
            film.getGenres().addAll(new HashSet<>(filmGenres.getOrDefault(film.getId(), Collections.emptyList())));
        });
        return films;
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

        film.getLikes().remove(userId);

        log.info("Пользователь с id {} удали лайк из фильма {}", userId, film.getName());
    }
}



//    public List<Film> popular(int size) {
//        // Шаг 1: Получите популярные фильмы
//        List<Film> films = filmStorage.getPopularFilms(size);
//
//        // Шаг 2: Получите все жанры и жанры для фильмов
//        List<Genre> allGenre = genreStorage.findAll();
//        List<FilmAndGenre> allFilmsAndGenre = filmAndGenreStorage.findAllFilmsAndGenre();
//
//        // Создайте отображение фильма на список жанров
//        Map<Long, List<Genre>> filmGenres = new HashMap<>();
//        for (FilmAndGenre filmAndGenre : allFilmsAndGenre) {
//            long filmId = filmAndGenre.getFilmId();
//            int genreId = filmAndGenre.getGenreId();
//            Genre genre = allGenre.stream()
//                    .filter(g -> g.getId() == genreId)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("Жанр не найден"));
//            filmGenres.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
//        }
//
//        // Шаг 3: Назначьте жанры фильмам
//        films.forEach(film -> {
//            if (film.getGenres() == null) {
//                film.setGenres(new HashSet<>());
//            }
//            film.getGenres().addAll(new HashSet<>(filmGenres.getOrDefault(film.getId(), Collections.emptyList())));
//        });
//
//        return films;
//    }



//    public List<Film> findAllFilm() {
//        List<Genre> allGenre = genreStorage.findAll();
//        List<Film> allFilm = filmStorage.findAllFilm();
//        List<FilmAndGenre> allFilmsAndGenre = filmAndGenreStorage.findAllFilmsAndGenre();
//
//        Map<Long, List<Genre>> filmGenres = new HashMap<>();
//        for (FilmAndGenre filmAndGenre : allFilmsAndGenre) {
//            long filmId = filmAndGenre.getFilmId();
//            int genreId = filmAndGenre.getGenreId();
//            Genre genre = allGenre.stream()
//                    .filter(g -> g.getId() == genreId)
//                    .findFirst()
//                    .orElseThrow(() -> new RuntimeException("Жанр не найден"));
//            filmGenres.computeIfAbsent(filmId, k -> new ArrayList<>()).add(genre);
//        }
//
//        allFilm.forEach(film -> {
//            if (film.getGenres() == null) {
//                film.setGenres(new HashSet<>());
//            }
//            film.getGenres().addAll(new HashSet<>(filmGenres.getOrDefault(film.getId(), Collections.emptyList())));
//        });
//        return allFilm;
//    }