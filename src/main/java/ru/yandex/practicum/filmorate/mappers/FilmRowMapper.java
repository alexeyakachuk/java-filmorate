package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashSet;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate())
                .duration(resultSet.getInt("duration"))
                .likes(new HashSet<>())
                .mpa(Mpa.builder()
                        .id(resultSet.getInt("mpa_id"))
                        .name(resultSet.getString("mpa.name"))
                        .build())
                .genres(new HashSet<>(Collections.singletonList(
                        Genre.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .build()
                )))
                .build();


    }
}
