package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage{
    private final NamedParameterJdbcOperations jdbcOperations;
    private final MpaRowMapper mapper;

    @Override
    public List<Mpa> findAll() {
        String query = "SELECT * FROM mpa";
        return jdbcOperations.query(query, mapper);
    }

    @Override
    public Mpa findById(int id) {
        String query = "SELECT * FROM mpa WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcOperations.queryForObject(query, params, mapper);
    }
}
