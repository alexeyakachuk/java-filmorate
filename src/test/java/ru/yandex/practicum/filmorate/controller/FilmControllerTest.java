package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    public void init() {
        filmController = new FilmController();
    }

    @Test
    void createTest() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        filmController.create(film);

        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(130)
                .build();
        filmController.create(film1);

        Map<Long, Film> films = filmController.getFilms();

        assertEquals(2, films.size());
    }

    @Test
    void updateTest() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        filmController.create(film);

        Film film1 = Film.builder()
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(150)
                .id(film.getId())
                .build();


        filmController.update(film1);

        Film updatedFilm = filmController.findAll().getFirst();

        assertEquals("new name", updatedFilm.getName());
        assertEquals("new description", updatedFilm.getDescription());
        assertEquals(LocalDate.of(2001, 1, 1), updatedFilm.getReleaseDate());
        assertEquals(150, updatedFilm.getDuration());
    }

    @Test
    void findAllTest() {

        Film film = Film.builder()
                .name("t")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        filmController.create(film);

        Film film1 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(130)
                .build();
        filmController.create(film1);

        Film film2 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(130)
                .build();
        filmController.create(film2);

        List<Film> films = filmController.findAll();

        assertEquals(3, films.size());

        List<Film> sortedFilms = new ArrayList<>(films);
        sortedFilms.sort(Comparator.comparing(Film::getName)
                .thenComparing(Film::getReleaseDate));

        assertEquals(sortedFilms, films);

    }
}
