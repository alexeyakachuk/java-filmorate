package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> findAll();

    Genre findById(int id);

    List<Genre> findByFilmId(long filmId);


    void addGenresToFilm(Film film);

    List<Genre> findGenresByFilmId(long filmId);
}
