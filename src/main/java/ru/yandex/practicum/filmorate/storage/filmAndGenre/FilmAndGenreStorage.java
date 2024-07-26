package ru.yandex.practicum.filmorate.storage.filmAndGenre;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmAndGenreStorage {

    List<FilmAndGenre> findAllFilmsAndGenre();
}
