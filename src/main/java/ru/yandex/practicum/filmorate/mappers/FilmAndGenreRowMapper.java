package ru.yandex.practicum.filmorate.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.filmAndGenre.FilmAndGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class FilmAndGenreRowMapper implements RowMapper<FilmAndGenre> {
    @Override
    public FilmAndGenre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new FilmAndGenre(resultSet.getLong("film_id"), resultSet.getInt("genre_id"));
    }
}
