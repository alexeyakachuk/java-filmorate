package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public List<Film> getPopularFilms(long size) {
        // Создаем параметры для запроса
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("size", size);

        // Определяем SQL-запрос для получения популярных фильмов с количеством лайков
        String sql = "SELECT films.*, COUNT(likes.user_id) AS like_count, mpa.name " +
                "FROM films " +
                "JOIN mpa ON films.mpa_id = mpa.id " +
                "LEFT JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id, mpa.name " +
                "ORDER BY like_count DESC " +
                "LIMIT :size";

        // Выполняем запрос и получаем результаты
        List<Film> films = jdbcOperations.query(sql, params, mapper);

        // Обработка случаев, когда список фильмов пуст
        if (films.isEmpty()) {
            log.info("Нет популярных фильмов для отображения");
        }


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

        return film;
    }


    @Override
    public void addLike(long filmId, long userId) {

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);
        jdbcOperations.update(
                "MERGE INTO likes (film_id, user_id) " +
                        "VALUES (:filmId, :userId)", params);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        params.addValue("userId", userId);

        jdbcOperations.update("DELETE FROM likes WHERE film_id = :filmId AND user_id = :userId", params);

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
    public Film updateFilm(Film updatedFilm) {
        // Обновление информации о фильме в таблице FILMS
        String updateFilmSql = "UPDATE films SET name = :name, description = :description, " +
                "release_date = :release_date, duration = :duration, mpa_id = :mpa_id " +
                "WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", updatedFilm.getId());
        params.addValue("name", updatedFilm.getName());
        params.addValue("description", updatedFilm.getDescription());
        params.addValue("release_date", updatedFilm.getReleaseDate());
        params.addValue("duration", updatedFilm.getDuration());
        params.addValue("mpa_id", updatedFilm.getMpa().getId());

        jdbcOperations.update(updateFilmSql, params);

        // Удаление старых жанров для фильма из таблицы FILM_GENRE
        String deleteGenresSql = "DELETE FROM film_genre WHERE film_id = :film_id";
        jdbcOperations.update(deleteGenresSql, new MapSqlParameterSource("film_id", updatedFilm.getId()));

        // Вставка новых жанров в таблицу FILM_GENRE, если они есть
        if (updatedFilm.getGenres() != null && !updatedFilm.getGenres().isEmpty()) {
            for (Genre genre : updatedFilm.getGenres()) {
                jdbcOperations.update(
                        "INSERT INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)",
                        new MapSqlParameterSource()
                                .addValue("film_id", updatedFilm.getId())
                                .addValue("genre_id", genre.getId()));
            }
        }

        // Возврат обновленного фильма с JOIN, чтобы получить данные по MPA
        String sql = "SELECT films.*, mpa.name AS mpa_name " +
                "FROM films " +
                "JOIN mpa ON films.mpa_id = mpa.id " +
                "WHERE films.id = :id";

        Film updatedFilmWithDetails = jdbcOperations.queryForObject(sql,
                new MapSqlParameterSource("id", updatedFilm.getId()), mapper);

        // Добавление жанров в объект фильма
        if (updatedFilmWithDetails != null) {
            String genreSql = "SELECT genres.* FROM film_genre " +
                    "JOIN genres ON film_genre.genre_id = genres.id " +
                    "WHERE film_genre.film_id = :film_id";
            List<Genre> genreList = jdbcOperations.query(genreSql,
                    new MapSqlParameterSource("film_id", updatedFilm.getId()), genreRowMapper);
            updatedFilmWithDetails.setGenres(new HashSet<>(genreList));
        }

        return updatedFilmWithDetails;
    }
}








