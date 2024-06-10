package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {

    @Test
    void createTest() {
        FilmController filmController = new FilmController();

        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(Duration.ofMinutes(120))
                .build();
        filmController.create(film);

        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2001,1,1))
                .duration(Duration.ofMinutes(130))
                .build();
        filmController.create(film1);

        Map<Long, Film> films = filmController.getFilms();

        assertEquals(2, films.size());
    }
}
