package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmControllerTest {


    InMemoryFilmStorage inMemoryFilmStorage;

    //    @BeforeEach
//    public void init() {
//        filmController = new FilmController();
//    }

    @Autowired
    public FilmControllerTest(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    @Test
    void createTest() {
        Film film = Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(120)
                .build();
        inMemoryFilmStorage.createFilm(film);

        Film film1 = Film.builder()
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(130)
                .build();
        inMemoryFilmStorage.createFilm(film1);

        Map<Long, Film> films = inMemoryFilmStorage.getFilms();

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
        inMemoryFilmStorage.createFilm(film);

        Film film1 = Film.builder()
                .name("new name")
                .description("new description")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(150)
                .id(film.getId())
                .build();


        inMemoryFilmStorage.updateFilm(film1);

        Film updatedFilm = inMemoryFilmStorage.findAllFilm().getFirst();

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
        inMemoryFilmStorage.createFilm(film);

        Film film1 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(2001, 1, 1))
                .duration(130)
                .build();
        inMemoryFilmStorage.createFilm(film1);

        Film film2 = Film.builder()
                .name("a")
                .description("description1")
                .releaseDate(LocalDate.of(1990, 1, 1))
                .duration(130)
                .build();
        inMemoryFilmStorage.createFilm(film2);

        List<Film> films = inMemoryFilmStorage.findAllFilm();

        assertEquals(3, films.size());

        List<Film> newListFilm = new ArrayList<>(films);

        assertEquals(newListFilm, films);

    }
}
