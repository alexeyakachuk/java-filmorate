package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FilmControllerTest {

    private FilmController filmController;
    private InMemoryUserStorage inMemoryUserStorage;


//    @BeforeEach
//    public void beforeEach() {
//        this.inMemoryUserStorage = new InMemoryUserStorage();
//        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), inMemoryUserStorage,
//                new GenreDbStorage(null, null)));
//    }

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

        Collection<Film> films = filmController.findAll();

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

        List<Film> newListFilm = new ArrayList<>(films);

        assertEquals(newListFilm, films);

    }

    @Test
    void addLikeTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        inMemoryUserStorage.createUser(user);
        long userId = user.getId();

        Film film = Film.builder()
                .name("Фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        filmController.create(film);
        long filmId = film.getId();

        filmController.addLike(filmId, userId);

        Film updatedFilm = filmController.find(filmId);
        assertTrue(updatedFilm.getLikes().contains(userId));
    }

    @Test
    void deleteLikeTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        inMemoryUserStorage.createUser(user);
        long userId = user.getId();

        Film film = Film.builder()
                .name("Фильм")
                .description("Описание")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        filmController.create(film);
        long filmId = film.getId();

        filmController.addLike(filmId, userId);
        filmController.deleteLike(filmId, userId);

        Film updatedFilm = filmController.find(filmId);
        assertFalse(updatedFilm.getLikes().contains(userId));
    }

    @Test
    void findTopRatedFilmsTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        inMemoryUserStorage.createUser(user);
        long userId = user.getId();

        Film film1 = Film.builder()
                .name("Фильм 1")
                .description("Описание 1")
                .releaseDate(LocalDate.of(2020, 1, 1))
                .duration(100)
                .build();
        filmController.create(film1);
        long filmId1 = film1.getId();

        Film film2 = Film.builder()
                .name("Фильм 2")
                .description("Описание 2")
                .releaseDate(LocalDate.of(2021, 1, 1))
                .duration(110)
                .build();
        filmController.create(film2);
        long filmId2 = film2.getId();

        filmController.addLike(filmId1, userId);
        filmController.addLike(filmId2, userId);

        List<Film> topRatedFilms = filmController.findTopRatedFilms(10);
        assertTrue(topRatedFilms.contains(film1) && topRatedFilms.contains(film2));
    }
}
