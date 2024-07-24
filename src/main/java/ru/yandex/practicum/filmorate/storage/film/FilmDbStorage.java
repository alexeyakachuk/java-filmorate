package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.HashSet;
import java.util.List;
@Repository
@RequiredArgsConstructor
@Qualifier("H2FilmStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaStorage mpaStorage;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Film> findAllFilm() {
        String query = "SELECT * FROM films";
        return jdbcOperations.query(query, mapper);
    }

    @Override
    public Film findFilm(long id) {
        String query = "SELECT * FROM films WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        Film film = jdbcOperations.queryForObject(query, params, mapper);

        if(film == null) {
            throw new NotFoundException("Фильм с id " + id + " не найден");
        }

       // film.setMpa(mpaStorage.findById(film.getMpa().getId()));
        String sql = "SELECT G.* FROM film_genre AS F JOIN genres AS G ON F.genre_id = G.id WHERE F.film_id = :id GROUP BY G.id";
        List<Genre> genreList = jdbcOperations.query(sql, params, genreRowMapper);
        film.setGenres(new HashSet<>(genreList));
        return film;
    }

    @Override
    public Film createFilm(Film newFilm) {
        return null;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return null;
    }
}
