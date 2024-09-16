package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final FilmService filmService;
    private Film testFilm;
    private Film testFilm1;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        jdbcOperations.update("DELETE FROM film_genre", Map.of());
        jdbcOperations.update("DELETE FROM likes", Map.of());
        jdbcOperations.update("DELETE FROM films", Map.of());
        jdbcOperations.update("DELETE FROM friendships", Map.of());
        jdbcOperations.update("DELETE FROM users", Map.of());

        Film film = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2024, 1, 1))
                .duration(120)
                .mpa(Mpa.builder()
                        .id(1) // ID соответствующего рейтинга MPA
                        .name("G")
                        .build())
                .genres(Set.of(Genre.builder()
                        .id(1) // ID соответствующего жанра
                        .name("Комедия")
                        .build()))
                .build();

        testFilm = filmStorage.createFilm(film);

        Film film1 = Film.builder()
                .name("film")
                .description("description")
                .releaseDate(LocalDate.of(2024, 1, 1))
                .duration(110)
                .mpa(Mpa.builder()
                        .id(2) // ID соответствующего рейтинга MPA
                        .name("PG")
                        .build())
                .genres(Set.of(Genre.builder()
                        .id(2) // ID соответствующего жанра
                        .name("Драма")
                        .build()))
                .build();

        testFilm1 = filmStorage.createFilm(film1);

        User user = User.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        testUser = userStorage.createUser(user);

        User user1 = User.builder()
                .email("test@example1.com")
                .login("testlogin1")
                .name("Test User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        testUser2 = userStorage.createUser(user1);
    }

    @Test
    void findAllFilmTest() {
        List<Film> films = filmStorage.findAllFilm();
        assertEquals(2, films.size());
    }

    @Test
    void getPopularFilmsTest() {
        filmStorage.addLike(testFilm.getId(), testUser2.getId());

        List<Film> popularFilms = filmStorage.getPopularFilms(1);
        assertEquals(1, popularFilms.size());
        assertEquals(testFilm, popularFilms.getFirst());

    }

    @Test
    void findFilmTest() {
        Film findFilm = filmStorage.findFilm(testFilm.getId());
        assertNotNull(findFilm);
        assertEquals(testFilm, findFilm, "Фильмы должны совпадать");
    }

    @Test
    void addLikeTest() {
        filmStorage.addLike(testFilm.getId(), testUser.getId());
        Set<Long> likes = filmService.findFilm(testFilm.getId()).getLikes();

        assertEquals(1, likes.size());
    }

    @Test
    void deleteLikeTest() {
        filmStorage.addLike(testFilm.getId(), testUser.getId());
        filmStorage.addLike(testFilm.getId(), testUser2.getId());
        filmStorage.deleteLike(testFilm.getId(), testUser.getId());
        Set<Long> likes = filmService.findFilm(testFilm.getId()).getLikes();

        assertEquals(1, likes.size());
    }

    @Test
    void createFilmTest() {
        Film newFilm = Film.builder()
                .name("Новый фильм")
                .description("Описание нового фильма")
                .releaseDate(LocalDate.of(2023, 1, 1))
                .duration(100)
                .mpa(Mpa.builder()
                        .id(1)
                        .name("G")
                        .build())
                .genres(Set.of(Genre.builder()
                        .id(1)
                        .name("Комедия")
                        .build()))
                .build();

        Film createdFilm = filmStorage.createFilm(newFilm);
        assertNotNull(createdFilm.getId());
        assertEquals(newFilm.getName(), createdFilm.getName(), "Название фильма должно совподать");
        assertEquals(newFilm.getDescription(), createdFilm.getDescription(), "Описание должно совподать");
        assertEquals(newFilm.getReleaseDate(), createdFilm.getReleaseDate(), "Дата должна совподать");
        assertEquals(newFilm.getDuration(), createdFilm.getDuration(), "Продолжительность должна совподать");
        assertEquals(newFilm.getMpa(), createdFilm.getMpa(), "MPA должно совподать");
    }

    @Test
    void updateFilmTest() {
        testFilm.setName("Обновлённое название");
        Film updatedFilm = filmStorage.updateFilm(testFilm);
        assertEquals(testFilm.getId(), updatedFilm.getId());
        assertEquals(testFilm.getName(), updatedFilm.getName());
    }
}