package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAllFilm();

    Film findFilm(long id);

    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);
}
