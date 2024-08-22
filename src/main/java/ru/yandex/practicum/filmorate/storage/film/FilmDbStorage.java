package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.util.*;

@Repository
@RequiredArgsConstructor
@Qualifier("H2FilmStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaStorage mpaStorage;
    private final GenreRowMapper genreRowMapper;


    @Override
    public List<Film> findAllFilm() {
        String query = "SELECT films.*, mpa.name FROM films JOIN mpa ON films.mpa_id = mpa.id;";

        List<Film> films = jdbcOperations.query(query, mapper);

        return films;

    }

    @Override
    public Film findFilm(long id) {
        String query = "SELECT films.*, mpa.name FROM films JOIN mpa ON films.mpa_id = mpa.id WHERE films.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Film film = jdbcOperations.queryForObject(query, params, mapper);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

        String sql = "SELECT genres.* FROM film_genre JOIN genres ON film_genre.genre_id = genres.id " +
                "WHERE film_genre.film_id = :id";
        List<Genre> genreList = jdbcOperations.query(sql, params, genreRowMapper);
        film.setGenres(new HashSet<>(genreList));

        String sqlLike = "SELECT user_id FROM likes " +
                "WHERE film_id = :id";


        List<Long> userLikes = jdbcOperations.queryForList(sqlLike, params, Long.class);

        film.setLikes(new HashSet<>(userLikes));

        return film;
    }



    public void addLike(long filmId, long userId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);
        jdbcOperations.update(
                "MERGE INTO likes (film_id, user_id) " +
                        "VALUES (:filmId, :userId)", params);

    }


    @Override
    public Film createFilm(Film newFilm) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", newFilm.getName());
        params.addValue("description", newFilm.getDescription());
        params.addValue("release_date", newFilm.getReleaseDate());
        params.addValue("duration", newFilm.getDuration());
        params.addValue("mpa_id", newFilm.getMpa().getId());

        // Вставка фильма в таблицу FILMS
        jdbcOperations.update(
                "INSERT INTO films (name, description, release_date, duration, mpa_id) " +
                        "VALUES (:name, :description, :release_date, :duration, :mpa_id)",
                params, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        // Вставка жанров в таблицу FILM_GENRE, если они есть
        if (newFilm.getGenres() != null && !newFilm.getGenres().isEmpty()) {
            for (Genre genre : newFilm.getGenres()) {
                jdbcOperations.update(
                        "INSERT INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)",
                        new MapSqlParameterSource()
                                .addValue("film_id", filmId)
                                .addValue("genre_id", genre.getId()));
            }
        }

        // Вставка лайков в таблицу LIKES, если они есть
//        if (newFilm.getLikes() != null && !newFilm.getLikes().isEmpty()) {
//            for (Long userId : newFilm.getLikes()) {
//                jdbcOperations.update(
//                        "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)",
//                        new MapSqlParameterSource()
//                                .addValue("film_id", filmId)
//                                .addValue("user_id", userId));
//            }
//        }

        // Возврат созданного фильма с JOIN, чтобы получить данные по MPA
        String sql = "SELECT films.*, mpa.name AS mpa_name " +
                "FROM films " +
                "JOIN mpa ON films.mpa_id = mpa.id " +
                "WHERE films.id = :id";

        Film createdFilm = jdbcOperations.queryForObject(sql,
                new MapSqlParameterSource("id", filmId), mapper);

        // Добавление жанров в объект фильма
        if (createdFilm != null) {
            String genreSql = "SELECT genres.* FROM film_genre " +
                    "JOIN genres ON film_genre.genre_id = genres.id " +
                    "WHERE film_genre.film_id = :film_id";
            List<Genre> genreList = jdbcOperations.query(genreSql,
                    new MapSqlParameterSource("film_id", filmId), genreRowMapper);
            createdFilm.setGenres(new HashSet<>(genreList));
        }

        return createdFilm;
    }


    @Override
    public Film updateFilm(Film newFilm) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        Long filmId = newFilm.getId();
        params.addValue("id", filmId);
        params.addValue("name", newFilm.getName());
        params.addValue("description", newFilm.getDescription());
        params.addValue("release_date", newFilm.getReleaseDate());
        params.addValue("duration", newFilm.getDuration());
        params.addValue("mpa_id", newFilm.getMpa().getId());

        // Вставка фильма в таблицу FILMS
        jdbcOperations.update(
                "UPDATE films SET name = :name, description = :description, release_date = :release_date, duration = :duration, mpa_id = :mpa_id " +
                        "WHERE id = :id",
                params);


        // Вставка жанров в таблицу FILM_GENRE
        for (Genre genre : newFilm.getGenres()) {
            jdbcOperations.update(
                    "INSERT INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)",
                    new MapSqlParameterSource()
                            .addValue("film_id", filmId)
                            .addValue("genre_id", genre.getId()));
        }

//         Вставка лайков в таблицу LIKES
        for (Long userId : newFilm.getLikes()) {
            jdbcOperations.update(
                    "INSERT INTO likes (film_id, user_id) VALUES (:film_id, :user_id)",
                    new MapSqlParameterSource()
                            .addValue("film_id", filmId)
                            .addValue("user_id", userId));
        }

        // Возврат созданного фильма с JOIN
        String sql = "SELECT films.*, mpa.name AS mpa_name " +
                "FROM films " +
                "JOIN mpa ON films.mpa_id = mpa.id " +
                "WHERE films.id = :id";

        return jdbcOperations.queryForObject(sql,
                new MapSqlParameterSource("id", filmId), mapper);
    }
}

