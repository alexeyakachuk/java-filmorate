package ru.yandex.practicum.filmorate.storage.filmAndGenre;

import lombok.Data;


@Data
public class FilmAndGenre {
    private final long filmId;
    private final int genreId;
}
