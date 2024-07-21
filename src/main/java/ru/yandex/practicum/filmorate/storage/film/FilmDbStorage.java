package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public class FilmDbStorage implements FilmStorage{
    @Override
    public List<Film> findAllFilm() {
        return List.of();
    }

    @Override
    public Film findFilm(long id) {
        return null;
    }

    @Override
    public Film createFilm(Film newFilm) {
        return null;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return null;
    }
}
