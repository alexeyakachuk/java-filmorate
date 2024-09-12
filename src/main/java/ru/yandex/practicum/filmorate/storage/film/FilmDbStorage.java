package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaStorage mpaStorage;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Film> findAllFilm() {
        String query = "SELECT films.*, mpa.name FROM films JOIN mpa ON films.mpa_id = mpa.id";
        // String query = "SELECT films.* FROM films";
        List<Film> films = jdbcOperations.query(query, mapper);

        return films;
    }

    @Override
    public List<Film> getPopularFilms(long size) {
        // Создаем параметры для запроса
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("size", size);

        // Определяем SQL-запрос для получения популярных фильмов с количеством лайков
        String query = "SELECT films.*, COUNT(likes.user_id) AS like_count, mpa.name " +
                "FROM films " +
                "JOIN mpa ON films.mpa_id = mpa.id " +
                "LEFT JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id, mpa.name " +
                "ORDER BY like_count DESC " +
                "LIMIT :size";


        // Выполняем запрос и получаем результаты
        List<Film> films = jdbcOperations.query(query, params, mapper);

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
        newFilm.setId(filmId);

        Film film = findFilm(filmId);

        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        // Определяем SQL-запрос для обновления данных фильма
        String query = "UPDATE films SET name = :name, description = :description, release_date = :release_date, " +
                "duration = :duration, mpa_id = :mpa_id WHERE id = :id";

        // Подготавливаем параметры для запроса
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", updatedFilm.getId());
        params.addValue("name", updatedFilm.getName());
        params.addValue("description", updatedFilm.getDescription());
        params.addValue("release_date", updatedFilm.getReleaseDate());
        params.addValue("duration", updatedFilm.getDuration());
        params.addValue("mpa_id", updatedFilm.getMpa().getId());

        // Выполняем запрос на обновление
        int rowsAffected = jdbcOperations.update(query, params);

        // Проверяем, был ли обновлён фильм
        if (rowsAffected == 0) {
            throw new NotFoundException("Фильм с id " + updatedFilm.getId() + " не найден");
        }

        // Возвращаем обновлённый фильм
        return updatedFilm;
    }
}










