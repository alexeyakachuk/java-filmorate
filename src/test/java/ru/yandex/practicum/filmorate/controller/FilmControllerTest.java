package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

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

    @Test
    void updateTest() {
        FilmController filmController = new FilmController();

        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(Duration.ofMinutes(120))
                .build();
        filmController.create(film);

        film.setName("new name");
        film.setDescription("new description");
        film.setReleaseDate(LocalDate.of(2002,1,1));
        film.setDuration(Duration.ofMinutes(150));

        Film updatedFilm = filmController.update(film);

        assertEquals("new name", updatedFilm.getName());
        assertEquals("new description", updatedFilm.getDescription());
        assertEquals(LocalDate.of(2002,1,1), updatedFilm.getReleaseDate());
        assertEquals(Duration.ofMinutes(150), updatedFilm.getDuration());
    }

    @Test
    void findAllTest() {
        FilmController filmController = new FilmController();

        Film film = Film.builder()
                .name("t")
                .description("description")
                .releaseDate(LocalDate.of(2000,1,1))
                .duration(Duration.ofMinutes(120))
                .build();
        filmController.create(film);

        Film film1 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(2001,1,1))
                .duration(Duration.ofMinutes(130))
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(1990,1,1))
                .duration(Duration.ofMinutes(130))
                .build();
        filmController.create(film2);

        Collection<Film> films = filmController.findAll();

        assertEquals(3, films.size());

        List<Film> sortedFilms = new ArrayList<>(films);
        sortedFilms.sort(Comparator.comparing(Film::getName)
                .thenComparing(Film::getReleaseDate));

        assertEquals(sortedFilms, films);

    }
}
