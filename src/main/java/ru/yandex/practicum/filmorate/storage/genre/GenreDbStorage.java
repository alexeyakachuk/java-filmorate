package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRowMapper mapper;

    @Override
    public List<Genre> findAll() {
        String query = "SELECT * FROM genre";
        return jdbcOperations.query(query, mapper);
    }

    @Override
    public Genre findById(int id) {
        String query = "SELECT * FROM mpa WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcOperations.queryForObject(query, params, mapper);

    }
}
