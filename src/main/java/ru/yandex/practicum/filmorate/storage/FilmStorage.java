package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> findAll();

    Film createFilm(Film newFilm);

    Film updateFilm(Film newFilm);
}