package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRowMapper mapper;

    @Override
    public List<Genre> findAll() {
        String query = "SELECT * FROM genres";
        return jdbcOperations.query(query, mapper);
    }

  // @Override
    public Genre findById(long id) {
        String query = "SELECT * FROM genres WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcOperations.queryForObject(query, params, mapper);

    }

    @Override
    public List<Genre> findByFilmId(long filmId) {
        String query = "SELECT * FROM genres " +
                "JOIN film_genre ON genres.id = film_genre.genre_id " +
                " WHERE film_genre.film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        List<Genre> genres = jdbcOperations.query(query, params, mapper);
        return genres;
    }
}
